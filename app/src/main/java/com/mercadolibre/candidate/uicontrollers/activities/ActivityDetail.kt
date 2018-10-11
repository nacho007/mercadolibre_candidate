package com.mercadolibre.candidate.uicontrollers.activities

import android.os.Bundle
import android.view.View
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.ITEM_ID
import com.mercadolibre.candidate.constants.PICTURE_ID
import com.mercadolibre.candidate.constants.THUMBNAIL
import com.mercadolibre.candidate.model.ProductItemDescription
import com.mercadolibre.candidate.model.ProductItemPictures
import com.mercadolibre.candidate.services.Service
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityDetail : ActivityBase() {

    private var serviceDescription: Call<ProductItemDescription>? = null
    private var calledServiceDescription: Boolean = false
    private var itemId: String = ""

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

        itemId = intent.getStringExtra(ITEM_ID)
        pictureId = intent.getStringExtra(PICTURE_ID)
        thumbnail = intent.getStringExtra(THUMBNAIL)

        Picasso.get().load(thumbnail).into(activity_detail_image_view_product)
    }

    override fun onStart() {
        super.onStart()

        if (!calledServiceDescription) {
            callServiceDescription()
        }

        if (!calledServicePictures) {
//            callServicePictures()
        }
    }

    override fun onStop() {
        super.onStop()
        serviceDescription?.cancel()
        servicePictures?.cancel()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFinishAfterTransition()
        onBackPressed()
        return true
    }


    private fun callServiceDescription() {
        serviceDescription = retrofit.create<Service>(Service::class.java).itemDescription(itemId)
        activity_detail_progress_bar.visibility = View.VISIBLE

        serviceDescription?.enqueue(object : Callback<ProductItemDescription> {
            override fun onFailure(call: Call<ProductItemDescription>?, t: Throwable?) {
                onFailure(call as Call<*>)

            }

            override fun onResponse(call: Call<ProductItemDescription>?, response: Response<ProductItemDescription>?) {
                when {
                    response?.code() == 200 -> {
                        calledServiceDescription = true
                        activity_detail_text_view_description.text = response.body()?.plainText
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
                onFailure(call as Call<*>)

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


}