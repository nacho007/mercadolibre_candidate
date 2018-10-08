package com.mercadolibre.candidate.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Values(
        @SerializedName("id") var id: String,
        @SerializedName("name") var name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Values> {
        override fun createFromParcel(parcel: Parcel): Values {
            return Values(parcel)
        }

        override fun newArray(size: Int): Array<Values?> {
            return arrayOfNulls(size)
        }
    }
}