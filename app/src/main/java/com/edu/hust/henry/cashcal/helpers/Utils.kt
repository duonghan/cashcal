package com.edu.hust.henry.cashcal.helpers

import android.content.Context
import android.util.TypedValue

object Utils{
    fun dp2px(dp: Int, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                context.resources.displayMetrics).toInt()
    }
}