package com.mercadolibre.candidate.uicontrollers.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
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
import kotlinx.android.synthetic.main.layout_retry.*
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
        activity_results_textview_empty_list.visibility = View.GONE

        if (savedInstanceState != null) {
            calledService = savedInstanceState.getBoolean(CALLED_SERVICE_SEARCH)
            productsArrayList = savedInstanceState.getParcelableArrayList(PRODUCT_ITEM_ARRAY)
            availableFilters = savedInstanceState.getParcelableArrayList(FILTER_ARRAY)

            if (productsArrayList.orEmpty().isEmpty()) {
                showEmptyResultsLabel()
            } else {
                setProductItemAdapter()
            }
        }

        searchResult = intent.getStringExtra(SEARCH_STRING)

        toolbarResults?.title = getString(R.string.activity_results_title)

        setSupportActionBar(toolbarResults)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        adapter_product_item_progress_bar.visibility = View.GONE

        layout_retry_button.setOnClickListener {
            layout_retry_constraint_layout.visibility = View.GONE
            callService(View.VISIBLE)
        }

        activity_results_swipe_layout.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent)

        activity_results_swipe_layout.setOnRefreshListener {
            layout_retry_constraint_layout.visibility = View.GONE
            callService(View.GONE)
        }
    }


    /**
     * If not called, in onStart the service is called
     *
     */
    override fun onStart() {
        super.onStart()

        if (!calledService) {
            callService(View.VISIBLE)
        }
    }

    /**
     * Stop the service in onStop to avoid processing when the app is backgrounded
     *
     */
    override fun onStop() {
        super.onStop()
        service?.cancel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    /**
     * Saves the state of the service result for later use
     *
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList(PRODUCT_ITEM_ARRAY, productsArrayList)
        outState?.putParcelableArrayList(FILTER_ARRAY, availableFilters)
        outState?.putBoolean(CALLED_SERVICE_SEARCH, calledService)
    }

    private fun callService(progressBarVisibility: Int) {
        service = retrofit.create<Service>(Service::class.java).listSearchResultItems(SITE_ID, searchResult)
        adapter_product_item_progress_bar.visibility = progressBarVisibility
        activity_results_textview_empty_list.visibility = View.GONE

        service?.enqueue(object : Callback<SearchResultItem> {
            override fun onFailure(call: Call<SearchResultItem>?, t: Throwable?) {
                onFailure(call as Call<*>)
            }

            override fun onResponse(call: Call<SearchResultItem>?, response: Response<SearchResultItem>?) {
                when {
                    response?.code() == 200 -> {
                        cancelDialogError()
                        processServiceResponse(response.body())
                    }
                    else -> {
                        processRequest(response as Response<*>)
                    }
                }
            }
        })
    }

    override fun onFailure(call: Call<*>?) {
        super.onFailure(call)
        activity_results_swipe_layout.isRefreshing = false
        activity_results_swipe_layout.isEnabled = true
    }

    fun processServiceResponse(searchResultItem: SearchResultItem?) {
        calledService = true
        activity_results_swipe_layout.isRefreshing = false
        activity_results_swipe_layout.isEnabled = true
        adapter_product_item_progress_bar.visibility = View.GONE

        if (searchResultItem?.results?.size?.compareTo(0) != 0) {
            productsArrayList = searchResultItem?.results
            availableFilters = searchResultItem?.availableFilters

            activity_results_recycler_view.visibility = View.VISIBLE
            activity_results_textview_empty_list.visibility = View.GONE
            setProductItemAdapter()
        } else {
            productsArrayList = null
            activity_results_textview_empty_list.visibility = View.VISIBLE
            activity_results_recycler_view.visibility = View.GONE
        }
    }

    /**
     * This method is in charge of handling the layout rotation
     *
     */
    private fun setRecyclerView() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRecyclerViewLandscapeLayout()
        } else {
            setRecyclerViewPortraitLayout()
        }
    }

    /**
     * Sets the recyclerview for Portrait layout
     *
     */
    private fun setRecyclerViewPortraitLayout() {
        activity_results_recycler_view.setHasFixedSize(true)
        activity_results_recycler_view.layoutManager = LinearLayoutManager(this)

        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.shape_divider_list_item)
        activity_results_recycler_view.addItemDecoration(SimpleDividerItemDecoration(dividerDrawable))
    }

    /**
     * Sets the recyclerview for Landscape layout, using Grid manager with 2 columns
     *
     */
    private fun setRecyclerViewLandscapeLayout() {
        activity_results_recycler_view.setHasFixedSize(true)
        val spacing = resources.getDimension(R.dimen.margin).toInt()
        activity_results_recycler_view.addItemDecoration(ColumnItemDecoration(spacing))

        val layoutManager = GridLayoutManager(this, 2)
        activity_results_recycler_view.layoutManager = layoutManager
    }

    private fun setProductItemAdapter() {
        try {
            availableFilters?.forEach {
                if (it.id == CONDITION) {
                    ConditionMapper.instance.setValues(it.values)
                }
            }

        } catch (exception: Exception) {
            Log.e(tag, exception.toString())
        }

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ListItemBackgroundBuilder.instance.assignLandscapeBackgroundPositions(productsArrayList)
        } else {
            ListItemBackgroundBuilder.instance.assignPortraitBackgroundPositions(productsArrayList)
        }

        adapterProduct = AdapterProduct(productsArrayList, this)
        activity_results_recycler_view.adapter = adapterProduct
    }

    /**
     * Handling the Dialog Error onCancel event
     *
     */
    override fun onCancel(description: String) {
        adapter_product_item_progress_bar.visibility = View.GONE
        activity_results_swipe_layout.isRefreshing = false
        activity_results_swipe_layout.isEnabled = true

        if (productsArrayList.orEmpty().isEmpty()) {
            layout_retry_constraint_layout.visibility = View.VISIBLE
        } else {
            activity_results_recycler_view.visibility = View.VISIBLE
            layout_retry_constraint_layout.visibility = View.GONE
        }

    }

    /**
     * Handling the Dialog Error onRetry event
     *
     */
    override fun onRetry() {
        activity_results_recycler_view.visibility = View.GONE
        callService(View.VISIBLE)
    }


    /**
     * When a product is clicked, we navigate to the Product's detail activity
     *
     */
    override fun onProductItemClick(productItem: ProductItem?, view: View) {
        val intent = Intent(this, ActivityDetail::class.java)
        intent.putExtra(ITEM_ID, productItem?.id)
        intent.putExtra(ITEM_THUMBNAIL, productItem?.thumbnail)
        intent.putExtra(PICTURE_ID, productItem?.getPictureId())

        var options: ActivityOptionsCompat? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, getString(R.string.product_image))
        }

        startActivity(intent, options?.toBundle())
    }

    private fun showEmptyResultsLabel() {
        activity_results_textview_empty_list.visibility = View.VISIBLE
        activity_results_recycler_view.visibility = View.GONE
    }
}