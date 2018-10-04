package com.mercadolibre.candidate.model

import com.google.gson.annotations.SerializedName

class Filter(
        @SerializedName("id") var id: String,
        @SerializedName("values") var values: ArrayList<Values>
)