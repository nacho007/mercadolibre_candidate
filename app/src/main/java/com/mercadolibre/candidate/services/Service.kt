package com.mercadolibre.candidate.services

import com.mercadolibre.candidate.model.ProductItemDescription
import com.mercadolibre.candidate.model.ProductItemPictures
import com.mercadolibre.candidate.model.SearchResultItem

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface Service {

    @GET("sites/{site_id}/search")
    fun listSearchResultItems(@Path("site_id") siteId: String,
                              @Query("q") q: String): Call<SearchResultItem>

    @GET("/items/{item_id}/description")
    fun itemDescription(@Path("item_id") itemId: String): Call<ProductItemDescription>

    @GET("/pictures/{picture_id}")
    fun itemPictures(@Path("picture_id") pictureId: String): Call<ProductItemPictures>

}