package com.mercadolibre.candidate.preferences

import android.content.Context
import android.content.SharedPreferences
import com.mercadolibre.candidate.R

class Preferences private constructor(context: Context?) {

    private var sharedPref: SharedPreferences? = null

    init {
        sharedPref = context?.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }

    companion object : SingletonHolder<Preferences, Context>(::Preferences)

    fun saveSearchString(context: Context?, stringSearch: Set<String>?) {
        with(sharedPref?.edit()) {
            this?.clear()
            this?.putStringSet(context?.getString(R.string.preference_search_strings), stringSearch)?.apply()
        }
    }

    fun getSearchString(context: Context?): MutableSet<String>? {
        return sharedPref?.getStringSet(context?.getString(R.string.preference_search_strings), mutableSetOf()) as MutableSet
    }

}