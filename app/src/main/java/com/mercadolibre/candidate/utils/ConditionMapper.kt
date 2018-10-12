package com.mercadolibre.candidate.utils

import com.mercadolibre.candidate.model.Values

class ConditionMapper private constructor() {

    private var valuesArrayList: ArrayList<Values>? = null

    init {
        valuesArrayList = ArrayList()
    }

    private object Holder {
        val INSTANCE = ConditionMapper()
    }

    companion object {
        val instance: ConditionMapper by lazy { Holder.INSTANCE }
    }

    fun setValues(values: ArrayList<Values>?) {
        valuesArrayList = values
    }

    fun matchValue(value: String?): String? {
        return when (valuesArrayList) {
            null -> value
            else -> {
                var response = value
                for (i in 0 until valuesArrayList!!.size) {
                    if (valuesArrayList!![i].id == value) {
                        response = valuesArrayList!![i].name
                        break
                    }
                }
                return response
            }

        }
    }

}