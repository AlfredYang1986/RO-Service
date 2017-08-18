package bmlogic.estirmateAnalysis.util

import java.util.Date

/**
  * Created by clock on 17-7-10.
  */
object Compare {
    def compareYDiffer(src:Date,dst:Date): Int ={
        val srcCal = java.util.Calendar.getInstance()
        srcCal.setTime(src)
        val dstCal = java.util.Calendar.getInstance()
        dstCal.setTime(new Date())
        dstCal.get(java.util.Calendar.YEAR) - srcCal.get(java.util.Calendar.YEAR)
    }
}
