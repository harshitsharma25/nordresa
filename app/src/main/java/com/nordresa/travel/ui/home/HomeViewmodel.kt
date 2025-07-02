package com.nordresa.travel.ui.home


import androidx.lifecycle.ViewModel
import com.nordresa.travel.models.StopsData

class HomeViewModel : ViewModel() {
    var departureStopsData: StopsData? = null
    var destinationStopsData: StopsData? = null
}
