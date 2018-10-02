package com.mercadolibre.candidate.services

import com.mercadolibre.candidate.model.SearchResultItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Service {

    @GET("sites/{site_id}/search")
    fun listSearchResultItems(@Path("site_id") siteId: String,
                              @Query("q") q: String): Call<SearchResultItem>
}