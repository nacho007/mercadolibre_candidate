package com.mercadolibre.candidate.uicontrollers.activities

import android.os.Bundle
import android.widget.Toast
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.adapters.AdapterProduct
import com.mercadolibre.candidate.constants.SEARCH_STRING
import com.mercadolibre.candidate.constants.SITE_ID
import com.mercadolibre.candidate.model.SearchResultItem
import com.mercadolibre.candidate.services.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityResults : ActivityBase() {

    var service: Call<SearchResultItem>? = null
    var searchResult = ""

    private var adapterProduct: AdapterProduct? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        searchResult = intent.getStringExtra(SEARCH_STRING)
    }

    override fun onStart() {
        super.onStart()

        service = retrofit.create<Service>(Service::class.java).listSearchResultItems(SITE_ID, searchResult)

        service?.enqueue(object : Callback<SearchResultItem> {
            override fun onFailure(call: Call<SearchResultItem>?, t: Throwable?) {
                onFailure(call as Call<*>)
            }

            override fun onResponse(call: Call<SearchResultItem>?, response: Response<SearchResultItem>?) {
                when {
                    response?.code() == 200 -> {
                        Toast.makeText(applicationContext, response.body()?.siteID, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        processRequest(response as Response<*>)
                    }
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        service?.cancel()
    }
}