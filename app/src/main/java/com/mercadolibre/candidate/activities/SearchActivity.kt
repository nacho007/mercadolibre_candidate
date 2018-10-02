package com.mercadolibre.candidate.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mercadolibre.candidate.R
import com.mercadolibre.candidate.constants.BASE_URL
import com.mercadolibre.candidate.model.SearchResultItem
import com.mercadolibre.candidate.services.SearchService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)


        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()

        val service = retrofit.create<SearchService>(SearchService::class.java)

        service.listSearchResultItems("MLU", "chromecast").enqueue(object : Callback<List<SearchResultItem>> {
            override fun onFailure(call: Call<List<SearchResultItem>>?, t: Throwable?) {
                Toast.makeText(applicationContext, "onFailure", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<List<SearchResultItem>>?, response: Response<List<SearchResultItem>>?) {
                Toast.makeText(applicationContext, "onResponse", Toast.LENGTH_SHORT).show()
            }
        })


    }
}
