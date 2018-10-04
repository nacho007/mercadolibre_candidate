package com.mercadolibre.candidate.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchResultItem(
        @SerializedName("results") var results: ArrayList<ProductItem>,
        @SerializedName("available_filters") var availableFilters: ArrayList<Filter>
) : Serializable





