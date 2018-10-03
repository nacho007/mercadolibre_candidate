package com.mercadolibre.candidate.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchResultItem(
        @SerializedName("site_id") var siteID: String,
        @SerializedName("query") var query: String,
        @SerializedName("results") var results: ArrayList<ProductItem>
) : Serializable





