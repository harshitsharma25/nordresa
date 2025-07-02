package com.nordresa.travel.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nordresa.travel.data.remote.api.NoNetworkException
import com.nordresa.travel.data.remote.api.onApiError
import com.nordresa.travel.data.remote.api.onException
import com.nordresa.travel.data.remote.api.onSuccess
import com.nordresa.travel.data.remote.api.safeApiCall
import com.nordresa.travel.data.remote.network.HTTPStatus
import com.nordresa.travel.models.StopsData
import com.nordresa.travel.models.search_response.ProductAtStop
import com.nordresa.travel.models.search_response.StopLocation
import com.nordresa.travel.models.search_response.StopLocationOrCoordLocation
import com.nordresa.travel.repository.NordResaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class StopUiState{
    object Loading : StopUiState()
    object Empty :   StopUiState()
    data class StopData(val stops : List<StopsData>) : StopUiState()
    data class Error(val code : Int, val message : String?) : StopUiState()
    data class NoNetwork(val prevState : StopUiState) : StopUiState()
}

class SearchViewModel(private val repository: NordResaRepository) : ViewModel() {

    private var _uiState : MutableStateFlow<StopUiState> = MutableStateFlow(StopUiState.Empty)
    val uiState = _uiState.asStateFlow()


    fun fetchStopsData(cityName : String){

        _uiState.value = StopUiState.Loading

        viewModelScope.launch {
            safeApiCall {
                println("--> reached fetchStopsData() before api call")
                repository.getStopsByName(cityName)
            }.onSuccess { body ->
                println("--> reached fetchStopsData() after api call")
                val stops = body.stopLocationOrCoordLocation
                    .mapNotNull { it.toStopDataOrNull() }

                _uiState.value = if (stops.isEmpty()) {
                    StopUiState.Empty
                } else {
                    StopUiState.StopData(stops)
                }
            }.onApiError{ code, message ->
                _uiState.value = StopUiState.Error(
                    code,
                    message ?: "Something went wrong. Please try again later."
                )
            } .onException { e ->
                when (e) {
                    is NoNetworkException -> {
                        _uiState.value = StopUiState.NoNetwork(
                            _uiState.value
                        )
                    }
                    else -> {
                        _uiState.value = StopUiState.Error(
                            HTTPStatus.UNEXPECTED_RESPONSE.code,
                            HTTPStatus.UNEXPECTED_RESPONSE.message
                        )
                    }
                }
            }
        }
    }

    fun clearStops() {
        _uiState.value = StopUiState.Empty
    }

}

fun StopLocationOrCoordLocation.toStopDataOrNull(): StopsData? {
    val stop = this.StopLocation ?: return null

    // Ensure required fields are not null
    val name = stop.name ?: return null
    val extId = stop.extId ?: return null
    val lat = stop.lat ?: return null
    val lon = stop.lon ?: return null

    val transportTypes = stop.productAtStop
        ?.mapNotNull { it.toClsOrNull() }
        ?: emptyList()

    return StopsData(
        name = name,
        extId = extId,
        lat = lat,
        lon = lon,
        transportTypes = transportTypes
    )
}


fun ProductAtStop.toClsOrNull(): String? {
    return this.cls
}
