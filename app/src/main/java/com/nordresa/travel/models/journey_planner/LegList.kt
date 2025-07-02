package com.nordresa.travel.models.journey_planner

data class LegList(
    val Leg: List<Leg>
)

data class Leg(
    val Origin: Location,
    val Destination: Location,
    val Notes: Notes,
    val JourneyDetailRef: JourneyDetailRef,
    val JourneyStatus: String,
    val Product: List<Product>,
    val JourneyDetail: JourneyDetail,
    val id: String,
    val idx: Int,
    val name: String,
    val number: String,
    val category: String,
    val type: String,
    val reachable: Boolean,
    val waitingState: String,
    val direction: String,
    val directionFlag: String,
    val duration: String
)
