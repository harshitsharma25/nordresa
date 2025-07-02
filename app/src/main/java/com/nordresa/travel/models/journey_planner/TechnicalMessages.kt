package com.nordresa.travel.models.journey_planner

data class TechnicalMessages(
    val TechnicalMessage: List<TechnicalMessage>
)

data class TechnicalMessage(
    val value: String,
    val key: String
)

