package com.mercadolibre.candidate.uicontrollers.activities

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.adapters.AdapterProduct
import com.mercadolibre.candidate.adapters.dividers.SimpleDividerItemDecoration
import com.mercadolibre.candidate.adapters.helpers.ColumnItemDecoration
import com.mercadolibre.candidate.adapters.helpers.ListItemBackgroundBuilder
import com.mercadolibre.candidate.constants.*
import com.mercadolibre.candidate.interfaces.OnProductItemClickListener
import com.mercadolibre.candidate.model.Filter
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
    private var productsArrayList: ArrayList<ProductItem>? = null
    private var availableFilters: ArrayList<Filter>? = null
    private var calledService = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        setRecyclerView()

        if (savedInstanceState != null) {
            calledService = savedInstanceState.getBoolean(CALLED_SERVICE)
            productsArrayList = savedInstanceState.getParcelableArrayList(PRODUCT_ITEM_ARRAY)
            availableFilters = savedInstanceState.getParcelableArrayList(FILTER_ARRAY)
            setProductItemAdapter()
        }

        searchResult = intent.getStringExtra(SEARCH_STRING)

        toolbar?.title = getString(R.string.activity_results_title)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        activity_results_textview_empty_list.visibility = View.GONE
        adapter_product_item_progress_bar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()

        if (!calledService) {
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
    }

    override fun onStop() {
        super.onStop()
        service?.cancel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList(PRODUCT_ITEM_ARRAY, productsArrayList)
        outState?.putParcelableArrayList(FILTER_ARRAY, productsArrayList)
        outState?.putBoolean(CALLED_SERVICE, calledService)
    }

    private fun setRecyclerView() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRecyclerViewLandscapeLayout()
        } else {
            setRecyclerViewPortraitLayout()
        }
    }

    private fun setRecyclerViewPortraitLayout() {
        activity_results_recycler_view.setHasFixedSize(true)
        activity_results_recycler_view.layoutManager = LinearLayoutManager(this)

        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.shape_divider_list_item)
        activity_results_recycler_view.addItemDecoration(SimpleDividerItemDecoration(dividerDrawable))
    }

    private fun setRecyclerViewLandscapeLayout() {
        activity_results_recycler_view.setHasFixedSize(true)
        val spacing = resources.getDimension(R.dimen.margin).toInt()
        activity_results_recycler_view.addItemDecoration(ColumnItemDecoration(spacing))

        val layoutManager = GridLayoutManager(this, 2)
        activity_results_recycler_view.layoutManager = layoutManager
    }

    fun processServiceResponse(searchResultItem: SearchResultItem?) {
        calledService = true
        adapter_product_item_progress_bar.visibility = View.GONE

        if (searchResultItem?.results?.size?.compareTo(0) != 0) {
            productsArrayList = searchResultItem?.results
            availableFilters = searchResultItem?.availableFilters

            setProductItemAdapter()
        } else {
            productsArrayList = null
            activity_results_textview_empty_list.visibility = View.VISIBLE
        }
    }


    private fun setProductItemAdapter() {
        activity_results_textview_empty_list.visibility = View.GONE

        try {

            for (i in 0 until availableFilters!!.size) {
                if (availableFilters!![i].id == CONDITION) {
                    ConditionMapper.instance.setValues(availableFilters!![i].values)
                }
            }

        } catch (exception: Exception) {
            Log.e(tag, exception.toString())
        }

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ListItemBackgroundBuilder.instance.assignLandscapeBackgroundPositions(productsArrayList as ArrayList<ProductItem>)
        } else {
            ListItemBackgroundBuilder.instance.assignPortraitBackgroundPositions(productsArrayList as ArrayList<ProductItem>)
        }

        adapterProduct = AdapterProduct(productsArrayList!!, this)
        activity_results_recycler_view.adapter = adapterProduct
    }

    override fun onProductItemClick(productItem: ProductItem) {
        Toast.makeText(this, productItem.title, Toast.LENGTH_SHORT).show()
    }
}