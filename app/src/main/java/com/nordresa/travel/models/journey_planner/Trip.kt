package com.nordresa.travel.models.journey_planner

data class Trip(
    val Origin: Location,
    val Destination: Location,
    val ServiceDays: List<ServiceDay>,
    val LegList: LegList,
    val calculation: String,
    val TripStatus: TripStatus,
    val idx: Int,
    val tripId: String,
    val ctxRecon: String,
    val duration: String,
    val rtDuration: String,
    val checksum: String
)

data class Location(
    val name: String,
    val type: String,
    val id: String,
    val extId: String,
    val lon: Double,
    val lat: Double,
    val routeIdx: Int,
    val prognosisType: String,
    val time: String,
    val date: String,
    val minimumChangeDuration: String
)

data class ServiceDay(
    val planningPeriodBegin: String,
    val planningPeriodEnd: String,
    val sDaysR: String,
    val sDaysI: String,
    val sDaysB: String
)

data class TripStatus(
    val hintCode: Int
)

