package com.mercadolibre.candidate.model

import android.os.Parcel
import android.os.Parcelable
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
        @SerializedName("accepts_mercadopago") var acceptsMercadoPago: Boolean
) : Serializable, Parcelable, Base() {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeDouble(price)
        parcel.writeString(currencyId)
        parcel.writeInt(availableQuantity)
        parcel.writeString(buyingMode)
        parcel.writeString(condition)
        parcel.writeString(thumbnail)
        parcel.writeByte(if (acceptsMercadoPago) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductItem> {
        override fun createFromParcel(parcel: Parcel): ProductItem {
            return ProductItem(parcel)
        }

        override fun newArray(size: Int): Array<ProductItem?> {
            return arrayOfNulls(size)
        }
    }
}