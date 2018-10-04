package com.mercadolibre.candidate.model

import com.google.gson.annotations.SerializedName

class Values(
        @SerializedName("id") var id: String,
        @SerializedName("name") var name: String
)