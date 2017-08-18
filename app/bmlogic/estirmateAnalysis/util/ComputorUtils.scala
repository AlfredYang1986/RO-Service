package bmlogic.estirmateAnalysis.util

import java.math.BigDecimal

/**
  * Created by clock on 17-7-10.
  */
object ComputorUtils {
    // 默认除法运算精度
    private val DEF_DIV_SCALE = 10

    def round(v: Double, scale: Int): Double = {
        if (scale < 0)
            throw new IllegalArgumentException("The scale must be a positive integer or zero")

        val b = new java.math.BigDecimal(java.lang.Double.toString(v))
        val one = new java.math.BigDecimal("1")
        b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue
    }

    def mul(v1: Double, v2: Double): Double = {
        val b1 = new java.math.BigDecimal(v1)
        val b2 = new java.math.BigDecimal(v2)
        return b1.multiply(b2).doubleValue
    }

    def sub(v1: Double, v2: Double): Double = {
        val b1 = new java.math.BigDecimal(v1)
        val b2 = new java.math.BigDecimal(v2)
        return b1.subtract(b2).doubleValue
    }

}
