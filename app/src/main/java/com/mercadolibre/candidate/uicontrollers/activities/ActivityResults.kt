package com.mercadolibre.candidate.uicontrollers.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.adapters.AdapterProduct
import com.mercadolibre.candidate.adapters.dividers.SimpleDividerItemDecoration
import com.mercadolibre.candidate.adapters.helpers.ListItemBackgroundBuilder
import com.mercadolibre.candidate.constants.CONDITION
import com.mercadolibre.candidate.constants.SEARCH_STRING
import com.mercadolibre.candidate.constants.SITE_ID
import com.mercadolibre.candidate.interfaces.OnProductItemClickListener
import com.mercadolibre.candidate.model.ProductItem
import com.mercadolibre.candidate.model.SearchResultItem
import com.mercadolibre.candidate.services.Service
import com.mercadolibre.candidate.utils.ConditionMapper
import kotlinx.android.synthetic.main.activity_results.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityResults : ActivityBase(), OnProductItemClickListener {

    private var service: Call<SearchResultItem>? = null
    private var searchResult = ""

    private var adapterProduct: AdapterProduct? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        searchResult = intent.getStringExtra(SEARCH_STRING)

        toolbar?.title = getString(R.string.activity_results_title)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activity_results_recycler_view.setHasFixedSize(true)
        activity_results_recycler_view.layoutManager = LinearLayoutManager(this)

        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.shape_divider_list_item)
        activity_results_recycler_view.addItemDecoration(SimpleDividerItemDecoration(dividerDrawable))

        activity_results_textview_empty_list.visibility = View.GONE
        adapter_product_item_progress_bar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()

        service = retrofit.create<Service>(Service::class.java).listSearchResultItems(SITE_ID, searchResult)
        adapter_product_item_progress_bar.visibility = View.VISIBLE

        service?.enqueue(object : Callback<SearchResultItem> {
            override fun onFailure(call: Call<SearchResultItem>?, t: Throwable?) {
                onFailure(call as Call<*>)
            }

            override fun onResponse(call: Call<SearchResultItem>?, response: Response<SearchResultItem>?) {
                when {
                    response?.code() == 200 -> {
                        processServiceResponse(response.body())
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun processServiceResponse(searchResultItem: SearchResultItem?) {
        adapter_product_item_progress_bar.visibility = View.GONE

        if (searchResultItem?.results?.size?.compareTo(0) != 0) {
            activity_results_textview_empty_list.visibility = View.GONE

            try {

                for (i in 0 until searchResultItem?.availableFilters?.size!!) {
                    if (searchResultItem.availableFilters[i].id == CONDITION) {
                        ConditionMapper.instance.setValues(searchResultItem.availableFilters[i].values)
                    }
                }

            } catch (exception: Exception) {
                Log.e(tag, exception.toString())
            }

            ListItemBackgroundBuilder.instance.assignBackgroundPositions(searchResultItem?.results as ArrayList<*>)
            adapterProduct = AdapterProduct(searchResultItem.results, this)
            activity_results_recycler_view.adapter = adapterProduct
        } else {
            activity_results_textview_empty_list.visibility = View.VISIBLE
        }
    }

    override fun onProductItemClick(productItem: ProductItem) {
        Toast.makeText(this, productItem.title, Toast.LENGTH_SHORT).show()
    }
}