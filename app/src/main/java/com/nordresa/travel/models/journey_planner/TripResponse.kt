package com.nordresa.travel.models.journey_planner

data class TripResponse(
    val Trip: List<Trip>,
    val ResultStatus: ResultStatus,
    val TechnicalMessages: TechnicalMessages,
    val serverVersion: String,
    val dialectVersion: String,
    val planRtTs: String,
    val requestId: String,
    val scrB: String,
    val scrF: String
)
