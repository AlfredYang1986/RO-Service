package bmlogic.callQueue.jri

import bmutil.JRConfig._
import org.rosuda.JRI.Rengine
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by liwei on 2017/6/21.
  */
trait JRIImpl extends JRITrait {
    override def callAI(arg : String): JsValue = {
        var re = Rengine.getMainEngine
        try {
            if (re == null){ re = new Rengine(Array[String]("--vanilla"), false, new TextConsole()) }
            if (!re.waitForR){ println("Cannot load R") }
            val version = re.eval("R.version.string").asString
            println(s"R.version=$version")
            re.eval("print(\"Evaluate a variale\")")
            re.assign("arg", arg)
            re.assign("R_File_Path", R_File_Path)
            re.assign("R_Json_Path", R_Json_Path)
            re.eval("print(\"Evaluate a variale fininshed\")")
            re.eval("print(arg)")
            re.eval("print(R_File_Path)")
            re.eval("print(R_Json_Path)")
            re.eval(s"source('${R_File_Path}${R_File_Name}')")
            val rrv = re.eval("pass").asString()
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
