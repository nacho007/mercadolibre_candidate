package com.mercadolibre.candidate.uicontrollers.activities

import android.os.Bundle
import android.view.View
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.*
import com.mercadolibre.candidate.model.ProductItemDescription
import com.mercadolibre.candidate.model.ProductItemPictures
import com.mercadolibre.candidate.services.Service
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_retry.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityDetail : ActivityBase() {

    private var serviceDescription: Call<ProductItemDescription>? = null
    private var calledServiceDescription: Boolean = false
    private var itemId: String = ""
    private var itemDescription: String? = null

    private var servicePictures: Call<ProductItemPictures>? = null
    private var calledServicePictures: Boolean = false
    private var pictureId: String = ""
    private var thumbnail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        toolbarDetail?.title = getString(R.string.activity_detail_title)

        setSupportActionBar(toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (savedInstanceState != null) {
            calledServiceDescription = savedInstanceState.getBoolean(CALLED_SERVICE_DESCRIPTION)
            calledServicePictures = savedInstanceState.getBoolean(CALLED_SERVICE_PICTURES)
            itemDescription = savedInstanceState.getString(ITEM_DESCRIPTION)

            if (!itemDescription.isNullOrEmpty()) {
                activity_detail_progress_bar.visibility = View.GONE
                activity_detail_text_view_description.text = itemDescription
            }
        }

        itemId = intent.getStringExtra(ITEM_ID)
        pictureId = intent.getStringExtra(PICTURE_ID)
        thumbnail = intent.getStringExtra(THUMBNAIL)

        Picasso.get().load(thumbnail).into(activity_detail_image_view_product)

        layout_retry_button.setOnClickListener {
            layout_retry_constraint_layout.visibility = View.GONE
            callServiceDescription()
            callServicePictures()
        }
    }

    override fun onStart() {
        super.onStart()

        if (!calledServiceDescription) {
            callServiceDescription()
        }

        if (!calledServicePictures) {
            callServicePictures()
        }
    }

    override fun onStop() {
        super.onStop()
        serviceDescription?.cancel()
        servicePictures?.cancel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
//        supportFinishAfterTransition()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(CALLED_SERVICE_DESCRIPTION, calledServiceDescription)
        outState?.putBoolean(CALLED_SERVICE_PICTURES, calledServicePictures)
        outState?.putString(ITEM_DESCRIPTION, itemDescription)
    }

    private fun callServiceDescription() {
        serviceDescription = retrofit.create<Service>(Service::class.java).itemDescription(itemId)
        activity_detail_progress_bar.visibility = View.VISIBLE

        serviceDescription?.enqueue(object : Callback<ProductItemDescription> {
            override fun onFailure(call: Call<ProductItemDescription>?, t: Throwable?) {
                activity_detail_progress_bar.visibility = View.GONE
                onFailure(call as Call<*>)
            }

            override fun onResponse(call: Call<ProductItemDescription>?, response: Response<ProductItemDescription>?) {
                when {
                    response?.code() == 200 -> {
                        calledServiceDescription = true
                        itemDescription = response.body()?.plainText
                        activity_detail_text_view_description.text = itemDescription
                        activity_detail_progress_bar.visibility = View.GONE
                    }
                    else -> {
                        processRequest(response as Response<*>)
                    }
                }
            }
        })
    }


    private fun callServicePictures() {
        servicePictures = retrofit.create<Service>(Service::class.java).itemPictures(pictureId)
        activity_detail_progress_bar.visibility = View.VISIBLE

        servicePictures?.enqueue(object : Callback<ProductItemPictures> {
            override fun onFailure(call: Call<ProductItemPictures>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<ProductItemPictures>?, response: Response<ProductItemPictures>?) {
                when {
                    response?.code() == 200 -> {
                        calledServicePictures = true
//                        processServiceResponse(response.body())
                    }
                    else -> {
                        processRequest(response as Response<*>)
                    }
                }
            }
        })
    }

    override fun onCancel() {
        activity_detail_progress_bar.visibility = View.GONE
        layout_retry_constraint_layout.visibility = View.VISIBLE
    }

    override fun onRetry() {
        callServiceDescription()
        callServicePictures()
    }

}