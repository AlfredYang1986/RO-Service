package bmlogic.fop

import java.io._
import java.util.Date

import bmutil.DateUtils._
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.Json._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.MultipartFormData
import bmutil.FileConfig.Export_File_Path
import bmutil.alFileOpt._

/**
  * Created by clock on 2017/7/4.
  * Based on liwei's Client
  */
object SliceUpload {

    //  : 多文件上传后台代码
    //  : 多文件上传的核心是，前端的文件队列里面，文件一个一个排着队，等第一个文件上传完了，在上传第二个文件，
    //  : 前端反复多次调用这个方法，mulitiFIleFileName为当前正在上传的文件名
    def ManyFileSlice(data: MultipartFormData[TemporaryFile])(implicit error_handler: String => JsValue): JsValue = {
        try {
            val args = data.dataParts
            val user_name = args.get("user_name").map(x => x.head).getOrElse(throw new Exception("input args error"))
            val file_name = args.get("name").map(x => x.head).getOrElse(throw new Exception("input args error"))
            val dir_path = s"$Export_File_Path$user_name/"

            createDir(dir_path)
            val modify_date = timeStamp2yyyyMMdd(new Date().getTime)
            val file_path = dir_path+modify_date+"_"+file_name
            deleteFile(file_path)

            data.files.foreach { x =>
                MergeSliceFile(file_path, x.ref.file)
            }
            Json.toJson(Map("status" -> toJson("ok"),"file_path" -> toJson(file_path)))
        } catch {
            case ex: Exception => error_handler(ex.getMessage)
        }
    }

    //  : 实现原理，以读写的方式打开目标文件，将分片文件缓冲流输入
    def MergeSliceFile(outPath: String, tempFile: File) {
        var raFile: RandomAccessFile = null
        var inputStream: BufferedInputStream = null
        try {
            val dirFile = new File(outPath)
            dirFile.createNewFile()
            //  : 以读写的方式打开目标文件(rw)
            raFile = new RandomAccessFile(dirFile, "rw")
            raFile.seek(raFile.length)
            inputStream = new BufferedInputStream(new FileInputStream(tempFile))
            val buf = new Array[Byte](1024)
            var length = inputStream.read(buf)
            while (length  != -1) {
                raFile.write(buf, 0, length)
                length = inputStream.read(buf)
            }
        } catch {
            case ioex: IOException => throw new IOException(ioex.getMessage)
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (raFile != null) {
                    raFile.close()
                }
            } catch {
                case ex: Exception => throw new Exception(ex.getMessage)
            }
        }
    }

    def downloadFile(name : String) : Array[Byte] = {
        val filepath = Export_File_Path + name
        val file = new File(filepath)
        val reVal : Array[Byte] = new Array[Byte](file.length.intValue)
        new FileInputStream(file).read(reVal)
        reVal
    }
}
