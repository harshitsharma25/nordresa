package com.nordresa.travel.ui.journey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.nordresa.travel.data.remote.api.NoNetworkException
import com.nordresa.travel.data.remote.api.onApiError
import com.nordresa.travel.data.remote.api.onException
import com.nordresa.travel.data.remote.api.onSuccess
import com.nordresa.travel.data.remote.api.safeApiCall
import com.nordresa.travel.data.remote.network.HTTPStatus
import com.nordresa.travel.models.JourneyData
import com.nordresa.travel.models.route_planner.Trip
import com.nordresa.travel.repository.NordResaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class JourneyUiState {
    object Loading : JourneyUiState()
    object Empty : JourneyUiState()
    data class RouteData(val stops: List<JourneyData>) : JourneyUiState()
    data class Error(val code: Int, val message: String?) : JourneyUiState()
    data class NoNetwork(val prevState: JourneyUiState) : JourneyUiState()
}


class JourneyViewModel(
    private val repository: NordResaRepository
) : ViewModel() {

    var departureLatLng: LatLng? = null
    var destinationLatLng: LatLng? = null

    private val _uiState = MutableStateFlow<JourneyUiState>(JourneyUiState.Empty)
    val uiState = _uiState.asStateFlow()

    fun fetchJourneyData(originId: String, destinationId: String) {
        _uiState.value = JourneyUiState.Loading

        viewModelScope.launch {
            safeApiCall {
                println("--> reached fetchJourneyData() before api call")
                repository.getTrips(originId, destinationId)
            }.onSuccess { body ->
                println("--> reached fetchJourneyData() after api call")
                val stops = body.Trip.mapNotNull { (it as com.nordresa.travel.models.route_planner.Trip).toJourneyDataOrNull() }

                _uiState.value = if (stops.isEmpty()) {
                    JourneyUiState.Empty
                } else {
                    JourneyUiState.RouteData(stops)
                }
            }.onApiError { code, message ->
                _uiState.value = JourneyUiState.Error(
                    code,
                    message ?: "Something went wrong. Please try again later."
                )
            }.onException { e ->
                when (e) {
                    is NoNetworkException -> {
                        _uiState.value = JourneyUiState.NoNetwork(_uiState.value)
                    }

                    else -> {
                        _uiState.value = JourneyUiState.Error(
                            HTTPStatus.UNEXPECTED_RESPONSE.code,
                            HTTPStatus.UNEXPECTED_RESPONSE.message
                        )
                    }
                }
            }
        }
    }
}

// Extension function to extract JourneyData safely
fun com.nordresa.travel.models.route_planner.Trip.toJourneyDataOrNull(): JourneyData? {
    return try {
        JourneyData(
            departTime = Origin.time,
            destTime = Destination.time,
            duration = duration,
            tripId = tripId
        )
    } catch (e: Exception) {
        null
    }
}

