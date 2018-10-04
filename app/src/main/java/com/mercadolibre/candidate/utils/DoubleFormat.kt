package com.mercadolibre.candidate.utils

import java.text.DecimalFormat

class DoubleFormat private constructor() {

    private var decimalFormat: DecimalFormat = DecimalFormat()

    init {
        decimalFormat.isDecimalSeparatorAlwaysShown = false
    }

    private object Holder {
        val INSTANCE = DoubleFormat()
    }

    companion object {
        val instance: DoubleFormat by lazy { Holder.INSTANCE }
    }

    fun getDecimalFormatOnlyShowDecimalIfNotZero(): DecimalFormat {
        return decimalFormat
    }
}