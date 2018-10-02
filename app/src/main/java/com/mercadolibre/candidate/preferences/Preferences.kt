package com.mercadolibre.candidate.preferences

import android.content.SharedPreferences

class Preferences private constructor() {

    init {
        println("This ($this) is a singleton")
//        val sharedPref = activity.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    private object Holder {
        val INSTANCE = Preferences()
    }

    companion object {
        val instance: Preferences by lazy { Holder.INSTANCE }
    }

    val sharedPref: SharedPreferences? = null
}