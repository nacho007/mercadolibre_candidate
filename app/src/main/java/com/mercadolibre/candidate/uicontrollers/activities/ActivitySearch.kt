package com.mercadolibre.candidate.uicontrollers.activities


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.SEARCH_STRING
import com.mercadolibre.candidate.preferences.Singleton
import kotlinx.android.synthetic.main.activity_search.*


class ActivitySearch : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

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

        val items = arrayOf("Car", "House", "Dog", "Chromecast", "Shirt")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        activity_search_edittext.setAdapter(adapter)

        val hole = Singleton.getInstance(this).stringValue
        Log.e(tag, hole)
    }


    fun validateSearchInputs() {
        activity_search_search_button.isEnabled = activity_search_edittext.text.isNotEmpty()
    }

    private fun navigateToActivityResults(searchString: String) {
        val intent = Intent(this, ActivityResults::class.java)
        intent.putExtra(SEARCH_STRING, searchString)
        startActivity(intent)
    }

}
