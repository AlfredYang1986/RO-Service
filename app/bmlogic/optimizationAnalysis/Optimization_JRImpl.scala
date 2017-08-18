package bmlogic.optimizationAnalysis

import bmlogic.callQueue.jri.{JRITrait, TextConsole}
import org.rosuda.JRI.Rengine
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/7/13.
  */
trait Optimization_JRImpl extends JRITrait {
    override def callAI(arg: String): JsValue = {
        val str = arg.substring(0,arg.length-1).substring(4,arg.length-2)
        val pairs = str.split("->|,").grouped(2)
        val retention = pairs.map { case Array(k, v) => k -> v }.toMap

        val sb = new StringBuffer()
        var re = Rengine.getMainEngine
        try {
            if (re == null) {
                re = new Rengine(Array[String]("--vanilla"), false, new TextConsole)
                if (!re.waitForR) System.out.println("Cannot load R")
            }
            val version = re.eval("R.version.string").asString
            println(s"R.version=$version")
            re.eval("print(\"Evaluate a variale\")")

            System.out.println("用户名："+retention("user_name"))
            re.assign("user_name", retention("user_name"))
            System.out.println("产品名："+retention("product_name"))
            re.assign("product_name", retention("product_name"))
            System.out.println("时间："+retention("create_date"))
            re.assign("create_date", retention("create_date"))
            System.out.println("优化类型："+retention("optimization_type"))
            val optimizationType = retention("optimization_type")
            re.assign("opt_type", optimizationType)
            if(optimizationType.equals("Fixed Budget")){
                System.out.println("总预算："+retention("general_budget"))
                re.assign("target", retention("general_budget"))
            }else if(optimizationType.equals("Target Revenue")){
                System.out.println("目标收益："+retention("target_return"))
                re.assign("target",retention("target_return"))
            }
            System.out.println("重要渠道数量："+retention("main_channel_number"))
            re.assign("main_ch",retention("main_channel_number"))
            System.out.println("药品单价："+retention("priceper_unit"))
            re.assign("product_price", retention("priceper_unit"))
            System.out.println("调整因子："+ retention("base_scaling_factor"))
            re.assign("base_scale_factor", retention("base_scaling_factor"))
            System.out.println("毛利润："+ retention("gross_margin"))
            re.assign("gross_margin",  retention("gross_margin"))

            re.eval("print(\"Evaluate a variale fininshed\")")
            re.eval("a <- c(user_name, product_name, create_date)")
            re.eval("print(a)")
            re.eval("source('/home/clock/workSpace/blackMirror/other/销量分析.R')")
            re.eval("print(pass)")
            val rrv = re.eval("pass").toString
            re.end()
            rrv match {
                case x if x.equals("TRUE") => toJson(rrv)
                case _ => throw new Exception("r run-time error")
            }
        } catch {
            case e : Exception => throw new Exception("r run-time error")
        }
    }
}
