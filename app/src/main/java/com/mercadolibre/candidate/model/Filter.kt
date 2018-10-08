package com.mercadolibre.candidate.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Filter(
        @SerializedName("id") var id: String,
        @SerializedName("values") var values: ArrayList<Values>
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(), arrayListOf<Values>().apply {
        parcel.readList(this, Values::class.java.classLoader)
    })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeList(values)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Filter> {
        override fun createFromParcel(parcel: Parcel): Filter {
            return Filter(parcel)
        }

        override fun newArray(size: Int): Array<Filter?> {
            return arrayOfNulls(size)
        }
    }
}