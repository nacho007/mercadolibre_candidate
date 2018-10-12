package com.mercadolibre.candidate.model;

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by ignaciodeandreisdenis on 10/11/18.
 */

class ProductItemDescription(
        @SerializedName("text") var text: String,
        @SerializedName("plain_text") var plainText: String
) : Serializable