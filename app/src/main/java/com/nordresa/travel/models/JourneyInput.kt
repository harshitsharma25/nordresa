package com.nordresa.travel.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JourneyInput(
    val departure: StopsData,
    val destination: StopsData
) : Parcelable
