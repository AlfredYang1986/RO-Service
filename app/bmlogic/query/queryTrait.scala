package bmlogic.query

import play.api.libs.json.JsValue

/**
  * Created by clock on 17-7-14.
  */
trait queryTrait {
    val list:List[Map[String, JsValue]]

    def queryAll = list
    def getHeadSortByDate = list.sortBy(_("create_date").as[Long]).reverse.head
}
