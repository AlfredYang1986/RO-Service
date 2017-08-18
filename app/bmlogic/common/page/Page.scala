package bmlogic.common.page

import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson
import scala.collection.immutable.Map

/**
  * Created by Wli on 2017/2/14 0014.
  * 修订：liwei
  * 日期：03/08/2017
  */
object Page {

    def TAKE : Int = 10
    def SKIP(PAGE_CURRENT : Long) : Int = ((PAGE_CURRENT-1)*TAKE).toInt

    /**
      *
      * @param PAGE_CURRENT     Current Page
      * @param TOTLE_RECORD     Totle Record
      * @return
      */
    def Page(PAGE_CURRENT : Long, TOTLE_RECORD : Long) : JsValue = {
        var ROW_START = (PAGE_CURRENT-1)*TAKE+1
        var ROW_END = PAGE_CURRENT*TAKE
        if(TOTLE_RECORD == 0){
            ROW_START = 0
            ROW_END = 0
        }
        toJson(Map(
            "ROW_START" -> toJson(ROW_START),
            "ROW_END" -> toJson(ROW_END),
            "PAGE_CURRE" -> toJson(PAGE_CURRENT),
            "TOTLE_PAGE" -> toJson(if(TOTLE_RECORD%TAKE==0)(TOTLE_RECORD/TAKE)else(TOTLE_RECORD/TAKE+1)),
            "TOTLE_RECORD" -> toJson(TOTLE_RECORD),
            "SERIAL" -> toJson(SKIP(PAGE_CURRENT)+1)
        ))
    }
}
