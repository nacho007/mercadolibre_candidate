package com.mercadolibre.candidate.model;

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by ignaciodeandreisdenis on 10/11/18.
 */

class ProductItemPictures(
        @SerializedName("id") var id: String,
        @SerializedName("max_size") var maxSize: String,
        @SerializedName("variations") var variations: ArrayList<ProductItemPicture>
) : Serializable