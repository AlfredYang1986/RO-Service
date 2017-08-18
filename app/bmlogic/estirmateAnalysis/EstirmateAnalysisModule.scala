package bmlogic.estirmateAnalysis

import java.text.SimpleDateFormat
import java.util.Date

import bminjection.db.DBTrait
import bmlogic.estirmateAnalysis.EstirmateAnalysisMessage.{msg_AnalysisEstirmate, msg_ReportEstirmate}
import bmlogic.estirmateAnalysis.util.{Compare, ComputorUtils}
import bmlogic.query._
import bmmessages.{CommonModules, MessageDefines}
import bmpattern.ModuleTrait
import bmutil.errorcode.ErrorCode
import com.mongodb.casbah.Imports._
import play.api.libs.json.{JsString, JsValue}
import play.api.libs.json.Json.toJson
import bmutil.precisionOpt.decimal2percent

/**
  * Created by clock on 2017/7/10.
  */
object EstirmateAnalysisModule extends ModuleTrait with Estirmate_JRImpl{
    def dispatchMsg(msg : MessageDefines)(pr : Option[Map[String, JsValue]])(implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = msg match {
        case msg_ReportEstirmate(data) => reportEstirmate(data)(pr)
        case msg_AnalysisEstirmate(data) => analysisEstirmate(data)(pr)
        case _ => ???
    }

    def analysisEstirmate(data : JsValue)
                         (pr : Option[Map[String, JsValue]])
                         (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            val retention_tb = Retention(DBObject(),db)
            val retention = retention_tb.getHeadSortByDate

            callAI(retention.toString)

            (Some(Map("method" -> toJson("评估分析"), "result" -> toJson("分析完成"))), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }



    def reportEstirmate(data : JsValue)
                       (pr : Option[Map[String, JsValue]])
                       (implicit cm : CommonModules) : (Option[Map[String, JsValue]], Option[JsValue]) = {
        try {
            val db = cm.modules.get.get("db").map (x => x.asInstanceOf[DBTrait]).getOrElse(throw new Exception("no db connection"))
            //FIXME 优化查找自然销量表中,当前操作人最后录入的document
            var arg = pr.get("result").asOpt[List[Map[String, JsValue]]].get.sortBy(_("create_date").as[Long]).reverse.head

            val tb = Map(
                "cct" -> ChannelCost(condition(arg),db),
                "et" -> Evaluation(condition(arg),db),
                "ot" -> Optimization(condition(arg),db),
                "rcpt" -> ResponseCurvePlot(condition(arg),db)
            )

            val red_fonts = getRedFonts(arg,tb)
            val table = getTable(tb)
            val echarts = getEcharts(tb)

            (Some(Map(
                "method" -> toJson("report estirmate"),
                "red_fonts" -> toJson(red_fonts),
                "tables" -> toJson(table),
                "echarts" -> toJson(echarts)
            )), None)
        } catch {
            case ex : Exception => (None, Some(ErrorCode.errorToJson(ex.getMessage)))
        }
    }

    def getRedFonts(arg: Map[String, JsValue],tb:Map[String,queryTrait]): Map[String,JsValue] = {
        val channel_cost_tb = tb("cct").queryAll
        val evaluation_tb = tb("et").queryAll
        val optimization_groupBy_cc = tb("ot").asInstanceOf[Optimization].groupByCC

        val nsd = calc_time(arg) ++ calc_sales_growth(channel_cost_tb)
        // nsd 中需要有total_sales_adj 要放到calc_sales_growth后面
        nsd ++ calc_carry_over_range(nsd,evaluation_tb) ++
            calc_channel_cost(optimization_groupBy_cc) ++
            calc_contribution(evaluation_tb) ++
            calc_mroi(optimization_groupBy_cc,evaluation_tb)
    }
    def calc_time(arg:Map[String,JsValue]): Map[String, JsValue] ={
        val dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyy-MM")
        val createDate = dateFormat.format(arg("create_date").asOpt[Long].get)
        val createDateStr = createDate.substring(0, 4) + "年" + createDate.substring(5, createDate.length) + "月"
        val launchDate = arg("launch_date").asOpt[String].get
        val dateFormat2:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
        val productFewYears = Compare.compareYDiffer(dateFormat2.parse(launchDate),new Date)
        val competition_level = arg("competition_level").asOpt[String].get

        arg ++ Map(
            "create_date" -> toJson(createDate),
            "create_date_str" -> toJson(createDateStr),
            "product_few_years" -> toJson(productFewYears),
            "product_mature_stage" -> toJson(if(productFewYears >=5) "成熟" else "相对较新"),
            "competition_level" -> toJson(if(competition_level == "高") "激烈" else if(competition_level == "中") "竞争相对激烈" else "一般")
        )
    }
    def calc_sales_growth(channel_cost_result_list:List[Map[String, JsValue]]): Map[String, JsValue] ={
        val channel_cost_sortBy_year = channel_cost_result_list.groupBy{x =>
            x.get("year")
            x.get("total_sales")
            x.get("total_sales_adj")
            //TODO 加上分组结果唯一 x.get("sales_growth")
        }.toList.sortBy{y => y._2.head("year").asOpt[Int].get}.reverse.map(_._2.head)
        val start_year = channel_cost_sortBy_year(1)("year")
        val total_sales = ComputorUtils.round(channel_cost_sortBy_year(1)("total_sales").as[Double]/100000000,2)
        val end_year = channel_cost_sortBy_year.headOption.get("year")
        val total_sales_adj = channel_cost_sortBy_year.headOption.get("total_sales_adj")
        val sales_growth = channel_cost_sortBy_year.headOption.get("sales_growth").as[Double]

        val month = channel_cost_result_list.groupBy{x =>
            x.get("year")
            x.get("month")
        }.toList.map(_._2.head).length

        Map(
            "start_year" -> toJson(start_year),
            "total_sales" -> toJson(total_sales),
            "end_year" -> toJson(end_year),
            "total_sales_adj" -> toJson(total_sales_adj),
            "sales_growth" -> toJson(decimal2percent(sales_growth)+"%"),
            "month" -> toJson(month)
        )
    }
    def calc_channel_cost(optimization_result:List[Map[String, JsValue]]): Map[String, JsValue] ={
        val channel_name1 = optimization_result.head("channel_name_en").asOpt[String].get
        val channel_name1_cost = optimization_result.head("current_cost").asOpt[Double].get
        val channel_name2 = optimization_result(1)("channel_name_en").asOpt[String].get
        val channel_name2_cost = optimization_result(1)("current_cost").asOpt[Double].get
        val channel_name3 = optimization_result(2)("channel_name_en").asOpt[String].get
        val channel_name3_cost = optimization_result(2)("current_cost").asOpt[Double].get

        val b0 = new java.math.BigDecimal(100.00)
        val b1 = new java.math.BigDecimal(channel_name1_cost)
        val b2 = new java.math.BigDecimal(channel_name2_cost)
        val b3 = new java.math.BigDecimal(channel_name3_cost)
        val channel_name_other = b0.subtract(b1.add(b2).add(b3)).doubleValue()

        Map(
            "channel_name1" -> toJson(channel_name1),
            "channel_name1_cost" -> toJson(decimal2percent(channel_name1_cost)+"%"),
            "channel_name2" -> toJson(channel_name2),
            "channel_name2_cost" -> toJson(decimal2percent(channel_name2_cost)+"%"),
            "channel_name3" -> toJson(channel_name3),
            "channel_name3_cost" -> toJson(decimal2percent(channel_name3_cost)+"%"),
            "channel_name_other" -> toJson(decimal2percent(channel_name_other) + "%")
        )
    }
    def calc_carry_over_range(arg:Map[String,JsValue],evaluation_result:List[Map[String, JsValue]]): Map[String, JsValue] = {
        val carry_over_range = evaluation_result.headOption.get("carry_over_range")
        val channel_name2_invest_percentage = evaluation_result(1)("invest_percentage")
        val channel_name2_roi = if(evaluation_result(1)("roi").as[Double] > 2) "高" else if (evaluation_result(1)("roi").as[Double] < 1) "低" else "中"
        val channel_name2_gain_rate = if(evaluation_result(1)("gain_rate").as[Int] > 80) "高" else if(evaluation_result(1)("gain_rate").as[Int] < 60) "低" else "中"
        val channel_name2_final_retention = if(evaluation_result(1)("final_retention").as[Double] > 0.15) "较慢" else if(evaluation_result(1)("final_retention").as[Double] < 0.05) "较快" else "相对较快"
        val channel_name3_invest_percentage = evaluation_result(2)("invest_percentage")
        val channel_name3_roi = if(evaluation_result(2)("roi").as[Double] > 2) "高" else if (evaluation_result(2)("roi").as[Double] < 1) "低" else "中"
        val channel_name3_final_retention = if(evaluation_result(2)("final_retention").as[Double] > 0.15) "较慢" else if(evaluation_result(2)("final_retention").as[Double] < 0.05) "较快" else "相对较快"
        val channel_name3_gain_rate = if(evaluation_result(2)("gain_rate").as[Int] > 80) "高" else if(evaluation_result(2)("gain_rate").as[Int] < 60) "低" else "中"
        val rSqure = ComputorUtils.round(evaluation_result.headOption.get("rSqure").as[Double],2)
        val total_sles_adj = arg("total_sales_adj").as[Double]
        val carryOver = evaluation_result.headOption.get("carry_over").as[Double]
        val carry_over1 = 100-carryOver
        val carry_over_total_sales_adj = ComputorUtils.round((100-carryOver)/total_sles_adj/100000000, 2)
        val total_sales_adj_carry_over = ComputorUtils.round(ComputorUtils.mul(total_sles_adj, carryOver/100000000), 2)

        arg ++ Map(
            "channel_name3_final_retention" -> toJson(channel_name3_final_retention),
            "channel_name3_gain_rate" -> toJson(channel_name3_gain_rate),
            "carry_over_range" -> toJson(carry_over_range),
            "channel_name2_invest_percentage" -> toJson(channel_name2_invest_percentage),
            "channel_name2_roi" -> toJson(channel_name2_roi),
            "channel_name2_gain_rate" -> toJson(channel_name2_gain_rate),
            "channel_name2_final_retention" -> toJson(channel_name2_final_retention),
            "channel_name3_invest_percentage" -> toJson(channel_name3_invest_percentage),
            "channel_name3_roi" -> toJson(channel_name3_roi),
            "carry_over" -> toJson(decimal2percent(carryOver)+"%"),
            "total_sales_adj_carry_over" -> toJson(total_sales_adj_carry_over),
            "rSqure" -> toJson(rSqure),
            "carry_over_total_sales_adj" -> toJson(carry_over_total_sales_adj),
            "carry_over1" -> toJson(decimal2percent(carry_over1)+"%")
        )
    }
    def calc_contribution(evaluation_result:List[Map[String, JsValue]]): Map[String, JsValue] ={
        val orderByContribution = evaluation_result.sortBy(x => x("contribution").as[Double]).reverse
        val contribution71 = orderByContribution.head("contribution").as[Double]
        val contribution72 = orderByContribution(1)("contribution").as[Double]
        val channel_name7 = orderByContribution(2)("channel_name_en").as[String]
        val contribution_other = 100-orderByContribution.head("carry_over").as[Double]-orderByContribution.head("contribution").as[Double]-orderByContribution(2)("contribution").as[Double]

        val orderByFinalRetention = evaluation_result.sortBy(x => x("final_retention").as[Double]).reverse
        val channel_name81 = orderByFinalRetention.head("channel_name_en")
        val channel_name82 = orderByFinalRetention(1)("channel_name_en")
        val channel_name83 = orderByFinalRetention(2)("channel_name_en")
        val final_retention81 = orderByFinalRetention.head("final_retention").as[Double]
        val final_retention82 = orderByFinalRetention(1)("final_retention").as[Double]
        val final_retention83 = orderByFinalRetention(2)("final_retention").as[Double]

        //没有数据,移植会报错
        //        map.put("channelNameEn", etlist3.get(0).getChannelNameEn());
        //        List<ChannelRetentionPlotTemplate> cptlist = channelRetentionPlotTemplateService.queryOrderByRetentionMonth(map);
        //        int ii = 0;
        //        for (int i = 0; i < cptlist.size(); i++) {
        //            if(cptlist.get(i).getRetentionMonth()>=0.01){
        //                ii +=i;
        //            }
        //        }
        //        general.setMonth81(ii);	//第八部分可以留存超过几个月
        //        map.put("channelNameEn", etlist3.get(1).getChannelNameEn());
        //        List<ChannelRetentionPlotTemplate> cptlist1 = channelRetentionPlotTemplateService.queryOrderByRetentionMonth(map);
        //        int jj = 0;
        //        for (int j = 0; j < cptlist1.size(); j++) {
        //            if(cptlist.get(j).getRetentionMonth()>=0.01){
        //                jj +=j;
        //            }
        //        }
        //        general.setMonth82(jj);	//第八部分可以留存超过几个月)

        Map(
            "channel_name71_contribution" -> toJson(decimal2percent(contribution71) + "%"),
            "channel_name72" -> toJson(channel_name7),
            "channel_name72_contribution" -> toJson(decimal2percent(contribution72) + "%"),
            "channel_name7_contribution_other" -> toJson(decimal2percent(contribution_other) + "%"),
            "channel_name81" -> toJson(channel_name81),
            "final_retention81" -> toJson(decimal2percent(final_retention81) + "%"),
            "channel_name82" -> toJson(channel_name82),
            "final_retention82" -> toJson(decimal2percent(final_retention82) + "%"),
            "channel_name83" -> toJson(channel_name83),
            "final_retention83" -> toJson(decimal2percent(final_retention83) + "%"),
            "month81" -> toJson(0),
            "month82" -> toJson(0)
        )
    }
    def subtractStr(t:Int, mroi:Double): String = {
        var message = ""

        t match {
            case 1 =>
                message = if (ComputorUtils.sub(mroi, 2.0) > 0) "比较高的位置"
                else if (ComputorUtils.sub(mroi, 1) < 0) "比较低的位置"
                else  "中等的位置"
            case 2 =>
                message = if (ComputorUtils.sub(mroi, 2.0) > 0) "更大"
                else if (ComputorUtils.sub(mroi, 1) < 0)  "极少"
                else "中等"
            case _ =>
                message = if (ComputorUtils.sub(mroi, 2.0) > 0) "很大的潜力"
                else if (ComputorUtils.sub(mroi, 1) < 0)  "几乎没有潜力"
                else  "较大的潜力"
        }
        message
    }
    def calc_mroi(optimization_result:List[Map[String, JsValue]],
                  evaluation_result:List[Map[String, JsValue]]): Map[String, JsValue] ={
        val et4 = evaluation_result.filter(_("channel_name_en").asOpt[String].get == optimization_result.head("channel_name_en").asOpt[String].get).head
        val et5 = evaluation_result.filter(_("channel_name_en").asOpt[String].get == optimization_result(1)("channel_name_en").asOpt[String].get).head
        val et6 = evaluation_result.filter(_("channel_name_en").asOpt[String].get == optimization_result(2)("channel_name_en").asOpt[String].get).head
        val et7 = evaluation_result.filter(_("channel_name_en").asOpt[String].get == optimization_result(3)("channel_name_en").asOpt[String].get).head
        Map(
            "channel_name91" -> toJson(et4("channel_name_en")),
            "roi91" -> toJson(decimal2percent(et4("roi").as[Double])),
            "mroi911" -> toJson(subtractStr(1,et4("mroi").as[Double])),
            "mroi912" -> toJson(subtractStr(2,et4("mroi").as[Double])),
            "mroi913" -> toJson(subtractStr(3,et4("mroi").as[Double])),
            "channel_name101" -> toJson(et5("channel_name_en")),
            "roi101" -> toJson(decimal2percent(et5("roi").as[Double])),
            "mroi1011" -> toJson(subtractStr(1,et5("mroi").as[Double])),
            "mroi1012" -> toJson(subtractStr(2,et5("mroi").as[Double])),
            "mroi1013" -> toJson(subtractStr(3,et5("mroi").as[Double])),
            "channel_name111" -> toJson(et6("channel_name_en")),
            "roi111" -> toJson(decimal2percent(et6("roi").as[Double])),
            "mroi1111" -> toJson(subtractStr(1,et6("mroi").as[Double])),
            "mroi1112" -> toJson(subtractStr(2,et6("mroi").as[Double])),
            "mroi1113" -> toJson(subtractStr(3,et6("mroi").as[Double])),
            "channel_name121" -> toJson(et7("channel_name_en")),
            "roi121" -> toJson(decimal2percent(et7("roi").as[Double])),
            "mroi1211" -> toJson(subtractStr(1,et7("mroi").as[Double])),
            "mroi1212" -> toJson(subtractStr(2,et7("mroi").as[Double])),
            "mroi1213" -> toJson(subtractStr(3,et7("mroi").as[Double]))
        )
    }


    def getTable(tb:Map[String,queryTrait]): Map[String,JsValue] = {
        val evaluation_tb = tb("et").queryAll
        val optimization_tb = tb("ot").queryAll
        getChannelTable(evaluation_tb) ++ getContributionTable(optimization_tb)
    }
    def getChannelTable(evaluation_tb:List[Map[String, JsValue]]) = {
        Map(
            "channel" -> toJson(evaluation_tb)
        )
    }
    def getContributionTable(optimization_tb:List[Map[String, JsValue]]) = {
        var currentCostSum:Double = 0.00
        var currentContributionSum = 0.00
        for (temp <- optimization_tb) {
            currentCostSum += temp("current_cost").as[Double];
            currentContributionSum += temp("current_contribution").as[Double];
        }

        val contribution = optimization_tb :+ Map(
            "channel_name_en"->toJson("合"),
            "current_cost"->toJson(currentCostSum/100),
            "current_contribution"->toJson(currentContributionSum/100)
        )
        Map(
            "contribution" -> toJson(contribution)
        )
    }


    def getEcharts(tb:Map[String,queryTrait]): Map[String,JsValue] = {
        val evaluation_tb = tb("et").queryAll
        val channel_cost_tb = tb("cct").queryAll
        val optimization_tb = tb("ot").queryAll
        val response_curve_plot_tb = tb("rcpt").queryAll
        val optimization_groupBy_cc = tb("ot").asInstanceOf[Optimization].groupByCC

        getPieEchart(evaluation_tb) ++
            getLine1Echart(channel_cost_tb) ++
            getLine2Echart(channel_cost_tb) ++ //TODO 未实现
            getBarEchart(evaluation_tb)++
            getCurve1Echart(optimization_groupBy_cc,response_curve_plot_tb) ++
            getCurve2Echart(optimization_groupBy_cc,response_curve_plot_tb) ++
            getCurve3Echart(optimization_groupBy_cc,response_curve_plot_tb) ++
            getCurve4Echart(optimization_groupBy_cc,response_curve_plot_tb)
    }
    def getPieEchart(evaluation_result:List[Map[String, JsValue]]) = {
        val channelNames = evaluation_result.map(x => x("channel_name_en").as[String])
        var seriesdata:List[Map[String,JsValue]] = Nil
        for (temp <- evaluation_result) {
           seriesdata = seriesdata :+ Map(
                "name" -> toJson(temp("channel_name_en").as[String]),
                "value" -> toJson(ComputorUtils.round(temp("cost_current").as[Int], 0))
            )
        }
        val pie = Map(
            "seriesdata" -> toJson(seriesdata),
            "legenddata" -> toJson(channelNames)
        )

        Map(
            "pie" -> toJson(pie)
        )
    }
    def getLine1Echart(channel_cost_result_list:List[Map[String, JsValue]]) = {
        val groupByYM_result = channel_cost_result_list.groupBy{x =>
            x.get("year")
            x.get("month")
        }.toList.sortBy{y => y._2.head("year").asOpt[Int].get}.map(_._2.head)

        val xAxisdata = groupByYM_result.map{x =>
            val year = x("year").toString
            val month = if(x("month").as[Int] < 10) "0"+x("month").toString else x("month").toString
            (year + month).toInt
        }

        val groupByCN_result = channel_cost_result_list.groupBy{x =>
            x.get("channel_name_en")
        }.toList.sortBy{y => y._2.head("year").asOpt[Int].get}.map(_._2.head)


        var seriesdata:List[Map[String,JsValue]] = Nil
        val normal = Map("normal" -> Map())
        for(temp <- groupByCN_result){
            val value = channel_cost_result_list.filter(_("channel_name_en").asOpt[String].get == temp("channel_name_en").asOpt[String].get).map(x=>x("cost").as[Int])
            seriesdata = seriesdata :+ Map(
                "name" -> toJson(temp("channel_name_en").as[String]),
                "type" -> toJson("line"),
                "stack" -> toJson("总量"),
                "areaStyle" -> toJson("normal:{}"),
                "data" -> toJson(value)
            )
        }

        val line1 = Map(
            "xAxisdata" -> toJson(xAxisdata),
            "seriesdata" -> toJson(seriesdata)
        )

        Map(
            "line1" -> toJson(line1)
        )
    }
    def getLine2Echart(channel_cost_result_list:List[Map[String, JsValue]]) = {
        val groupByYM_result = channel_cost_result_list.groupBy{x =>
            x.get("year")
            x.get("month")
        }.toList.sortBy{y => y._2.head("year").asOpt[Int].get}.map(_._2.head)

        val xAxisdata = groupByYM_result.map{x =>
            val year = x("year").toString
            val month = if(x("month").as[Int] < 10) "0"+x("month").toString else x("month").toString
            (year + month).toInt
        }

        val groupByCN_result = channel_cost_result_list.groupBy{x =>
            x.get("channel_name_en")
        }.toList.sortBy{y => y._2.head("year").asOpt[Int].get}.map(_._2.head)


        var seriesdata:List[Map[String,JsValue]] = Nil
        val normal = Map("normal" -> Map())
        for(temp <- groupByCN_result){
            val value = channel_cost_result_list.filter(_("channel_name_en").asOpt[String].get == temp("channel_name_en").asOpt[String].get).map(x=>x("cost").as[Int])
            seriesdata = seriesdata :+ Map(
                "name" -> toJson(temp("channel_name_en").as[String]),
                "type" -> toJson("line"),
                "stack" -> toJson("总量"),
                "areaStyle" -> toJson("normal:{}"),
                "data" -> toJson(value)
            )
        }

        val line1 = Map(
            "xAxisdata" -> toJson(xAxisdata),
            "seriesdata" -> toJson(seriesdata)
        )

        Map(
            "line2" -> toJson(line1)
        )
    }
    def getBarEchart(evaluation_result:List[Map[String, JsValue]]) = {
        val xAxisdata = evaluation_result.map(x=>x("channel_name_en").as[String])
        val seriesdata = evaluation_result.map(x=>x("roi").as[Double])
        val bar = Map(
            "xAxisdata" -> toJson(xAxisdata),
            "seriesdata" -> toJson(seriesdata)
        )
        Map(
            "bar" -> toJson(bar)
        )
    }
    def getCurve1Echart(optimization_result:List[Map[String, JsValue]],response_curve_plot_result:List[Map[String, JsValue]]) = {
        val channel_name = optimization_result(0)("channel_name_en").as[String]
        val result = response_curve_plot_result.filter(_("channel_name_en").as[String] == channel_name).sortBy(_("cost").as[Int])
        val xAxisdata = result.map(x => x("cost").as[Int])
        val seriesdata1 = result.map(x => x("cost").as[Int])
        val seriesdata2 = result.map(x => x("response").as[Double])

        val curve1 = Map(
            "xAxisdata" -> toJson(xAxisdata),
            "seriesdata1" -> toJson(seriesdata1),
            "seriesdata2" -> toJson(seriesdata2)
        )
        Map(
            "curve1" -> toJson(curve1)
        )
    }
    def getCurve2Echart(optimization_result:List[Map[String, JsValue]],response_curve_plot_result:List[Map[String, JsValue]]) = {
        val channel_name = optimization_result(1)("channel_name_en").as[String]
        val result = response_curve_plot_result.filter(_("channel_name_en").as[String] == channel_name).sortBy(_("cost").as[Int])
        val xAxisdata = result.map(x => x("cost").as[Int])
        val seriesdata1 = result.map(x => x("cost").as[Int])
        val seriesdata2 = result.map(x => x("response").as[Double])

        val curve1 = Map(
            "xAxisdata" -> toJson(xAxisdata),
            "seriesdata1" -> toJson(seriesdata1),
            "seriesdata2" -> toJson(seriesdata2)
        )
        Map(
            "curve2" -> toJson(curve1)
        )
    }
    def getCurve3Echart(optimization_result:List[Map[String, JsValue]],response_curve_plot_result:List[Map[String, JsValue]]) = {
        val channel_name = optimization_result(2)("channel_name_en").as[String]
        val result = response_curve_plot_result.filter(_("channel_name_en").as[String] == channel_name).sortBy(_("cost").as[Int])
        val xAxisdata = result.map(x => x("cost").as[Int])
        val seriesdata1 = result.map(x => x("cost").as[Int])
        val seriesdata2 = result.map(x => x("response").as[Double])

        val curve1 = Map(
            "xAxisdata" -> toJson(xAxisdata),
            "seriesdata1" -> toJson(seriesdata1),
            "seriesdata2" -> toJson(seriesdata2)
        )
        Map(
            "curve3" -> toJson(curve1)
        )
    }
    def getCurve4Echart(optimization_result:List[Map[String, JsValue]],response_curve_plot_result:List[Map[String, JsValue]]) = {
        val channel_name = optimization_result(3)("channel_name_en").as[String]
        val result = response_curve_plot_result.filter(_("channel_name_en").as[String] == channel_name).sortBy(_("cost").as[Int])
        val xAxisdata = result.map(x => x("cost").as[Int])
        val seriesdata1 = result.map(x => x("cost").as[Int])
        val seriesdata2 = result.map(x => x("response").as[Double])

        val curve1 = Map(
            "xAxisdata" -> toJson(xAxisdata),
            "seriesdata1" -> toJson(seriesdata1),
            "seriesdata2" -> toJson(seriesdata2)
        )
        Map(
            "curve4" -> toJson(curve1)
        )
    }

    def condition(arg: Map[String, JsValue]):DBObject = {
        val user_name = arg("user_name").asOpt[String].get
        val product_name = arg("product_name").asOpt[String].get
        val create_date = arg("create_date").asOpt[Long].get

        MongoDBObject(
            "user_name" -> user_name,
            "product_name" -> product_name,
            "create_date" -> create_date
        )
    }
}
