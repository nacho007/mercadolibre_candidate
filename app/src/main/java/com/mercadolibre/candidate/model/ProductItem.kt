package com.mercadolibre.candidate.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ProductItem(
        @SerializedName("id") var id: String,
        @SerializedName("title") var title: String,
        @SerializedName("price") var price: Double,
        @SerializedName("currency_id") var currencyId: String,
        @SerializedName("available_quantity") var availableQuantity: Int,
        @SerializedName("buying_mode") var buyingMode: String,
        @SerializedName("condition") var condition: String,
        @SerializedName("thumbnail") var thumbnail: String,
        @SerializedName("accepts_mercadopago") var acceptsMercadopago: Boolean
) : Serializable