package com.mercadolibre.candidate.uicontrollers.activities


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.SEARCH_STRING
import com.mercadolibre.candidate.preferences.Preferences
import kotlinx.android.synthetic.main.activity_search.*


class ActivitySearch : ActivityBase() {

    //We use a MUTABLESET for search strings, so there are no repeated Strings, also supported in shared Preferences!
    private var searchStringSet: MutableSet<String>? = null
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        activity_search_edittext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                validateSearchInputs()
            }
        })

        activity_search_search_button.setOnClickListener {
            navigateToActivityResults(activity_search_edittext.text.toString())
        }

        validateSearchInputs()

        activity_search_edittext.threshold = 1

        createSearchStringsAdapter()
    }

    private fun createSearchStringsAdapter() {
        searchStringSet = Preferences.getInstance(this).getSearchString(this)
        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, searchStringSet?.toMutableList()!!)
        activity_search_edittext.setAdapter(adapter)
    }

    private fun updateSearchStrings(searchString: String) {
        searchStringSet?.add(searchString)
        Preferences.getInstance(this).saveSearchString(this, searchStringSet)
        adapter?.add(searchString)
        adapter?.notifyDataSetChanged()
    }

    fun validateSearchInputs() {
        activity_search_search_button.isEnabled = activity_search_edittext.text.isNotEmpty()
    }

    private fun navigateToActivityResults(searchString: String) {
        updateSearchStrings(searchString)

        val intent = Intent(this, ActivityResults::class.java)
        intent.putExtra(SEARCH_STRING, searchString)
        startActivity(intent)
    }

}
