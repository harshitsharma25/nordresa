package com.nordresa.travel.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StopsData(
    val name: String,
    val extId: String,
    val lat: Double,
    val lon: Double,
    val transportTypes: List<String> = emptyList() // <- e.g., ["2", "4", "16"]
) : Parcelable
