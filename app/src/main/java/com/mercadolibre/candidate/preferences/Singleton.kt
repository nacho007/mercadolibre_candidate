package com.mercadolibre.candidate.preferences

import android.content.Context
import com.mercadolibre.candidate.R

class Singleton private constructor(context: Context) {

    var stringValue: String? = null

    init {
        // Init using context argument
        stringValue = context.getString(R.string.mobile_404)
    }

    companion object : SingletonHolder<Singleton, Context>(::Singleton)
}