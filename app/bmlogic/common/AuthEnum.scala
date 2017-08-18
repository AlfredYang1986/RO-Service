package bmlogic.common

/**
  * Created by liwei on 2017/8/2.
  */
object AuthEnum {
    def StringToInt(o : String) : Int = o match {
        case "超级管理员" => 0
        case "管理员" => 1
        case "普通用户" => 2
    }

    def IntToString(o : Int) : String = o match {
        case 0 => "超级管理员"
        case 1 => "管理员"
        case 2 => "普通用户"
    }
}
