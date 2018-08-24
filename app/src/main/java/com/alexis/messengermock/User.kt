package com.alexis.messengermock

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val uid: String, val email: String, val userImageUrl: String): Parcelable {
    constructor(): this("","", "")
}