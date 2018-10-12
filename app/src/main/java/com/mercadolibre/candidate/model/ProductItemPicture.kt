package com.mercadolibre.candidate.model;

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by ignaciodeandreisdenis on 10/11/18.
 */

class ProductItemPicture(
        @SerializedName("size") var size: String,
        @SerializedName("url") var url: String,
        @SerializedName("secure_url") var secure_url: String
) : Serializable