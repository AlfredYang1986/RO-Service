package bmlogic.resultsreport.FileExport

import java.util.UUID

import bmutil.FileConfig.Export_File_Path
import play.api.libs.json.JsValue
import bmutil.alFileOpt._

/**
  * Created by liwei on 2017/7/21.
  */
trait FileExportInfo {

    def FileExport(list: List[Map[String, JsValue]],suffix : String) : String

    def WriterIntoFile(header : Seq[Any],content : Seq[Seq[Any]],suffix : String) : String = {
        val filename = s"${UUID.randomUUID().toString}$suffix"
        write2CsvFile(Export_File_Path)(filename){cw =>
            cw.writeRow(header)
            cw.writeAll(content)
        }
        filename
    }
}
