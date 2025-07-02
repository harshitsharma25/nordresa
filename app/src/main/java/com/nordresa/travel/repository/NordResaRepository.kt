package com.nordresa.travel.repository

import com.nordresa.travel.data.remote.network.ApiClient
import com.nordresa.travel.models.search_response.StopResponse

class NordResaRepository {
    suspend fun getStopsByName(cityName : String) : StopResponse {
        val result = ApiClient.apiService.getStopsByName(cityName)
        return result
    }
}