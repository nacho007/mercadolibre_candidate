package com.mercadolibre.candidate.uicontrollers.activities


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.SEARCH_STRING
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
