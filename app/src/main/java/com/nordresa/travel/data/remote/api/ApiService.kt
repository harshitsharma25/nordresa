package com.nordresa.travel.data.remote.api

import com.nordresa.travel.models.route_planner.RoutePlannerResponse
import com.nordresa.travel.models.search_response.StopResponse
import com.nordresa.travel.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("location.name")
    suspend fun getStopsByName(
        @Query("input") input: String,
        @Query("format") format: String = "json",
        @Query("accessId") accessId: String = Constants.RESROBOT_API_KEY
    ): StopResponse

    @GET("trip")
    suspend fun searchTrips(
        @Query("originId") originId: String,
        @Query("destId") destId: String,
        @Query("format") format: String = "json",
        @Query("accessId") accessId: String = Constants.RESROBOT_API_KEY
    ): RoutePlannerResponse

}