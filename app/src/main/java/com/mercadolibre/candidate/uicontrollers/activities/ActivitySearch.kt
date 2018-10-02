package com.mercadolibre.candidate.uicontrollers.activities


import android.os.Bundle
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.SITE_ID
import com.mercadolibre.candidate.services.Service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActivitySearch : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        service = retrofit.create<Service>(Service::class.java).listSearchResultItems(SITE_ID, "chromecast")

        service?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                onFailure(call)
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                when {
                    response?.code() == 200 -> {
//                        val hola = response.body()?.contentType()?..toString()
//                        val hola = response.body().

//                        val jsonObject = JSONObject(response.body() as SearchResultItem)
//                        val jsonString = jsonObject.toString()

//                        val searchResultItem = gSon.fromJson(hola, SearchResultItem::class.java)
//                        Toast.makeText(applicationContext, searchResultItem.siteID, Toast.LENGTH_SHORT).show()
//                        Toast.makeText(applicationContext, "sadf", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        processRequest(response)
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
