package bmlogic.estirmateAnalysis

import bmlogic.callQueue.jri.{JRITrait, TextConsole}
import org.rosuda.JRI.Rengine
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by clock on 2017/7/13.
  */
trait Estirmate_JRImpl extends JRITrait {
    override def callAI(arg: String): JsValue = {
        val str = arg.substring(0,arg.length-1).substring(4,arg.length-2)
        val pairs = str.split("->|,").grouped(2)
        val estirmate_analysis = pairs.map { case Array(k, v) => k -> v }.toMap

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

            System.out.println("用户名："+estirmate_analysis("user_name"))
            re.assign("user_name", estirmate_analysis("user_name"))
            System.out.println("产品名："+estirmate_analysis("product_name"))
            re.assign("product_name",estirmate_analysis("product_name"))
            System.out.println("时间："+estirmate_analysis("create_date"))
            re.assign("create_date",estirmate_analysis("create_date"))

            re.eval("print(\"Evaluate a variale\")")
            re.eval("a <- c(user_name, product_name, create_date)")
            re.eval("print(a)")
            re.eval("source('D:/PET/R/Evaluation.R')")
            re.eval("print(\"Evaluate a variale fininshed\")")
            re.eval("arg <- c(arg)")
            re.eval("print(arg)")
            re.eval("source('/Users/liwei/Desktop/JR/销量分析.R')")
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
