package com.nordresa.travel.models.journey_planner

data class Notes(
    val Note: List<Note>
)

data class Note(
    val value: String,
    val key: String,
    val type: String,
    val routeIdxFrom: Int,
    val routeIdxTo: Int,
    val txtN: String
)

data class JourneyDetailRef(
    val ref: String
)

data class JourneyDetail(
    val ref: String,
    val dayOfOperation: String
)

data class Product(
    val icon: Icon,
    val operatorInfo: OperatorInfo,
    val name: String,
    val internalName: String,
    val displayNumber: String,
    val num: String,
    val line: String,
    val lineId: String,
    val catOut: String,
    val catIn: String,
    val catCode: String,
    val cls: String,
    val catOutS: String,
    val catOutL: String,
    val operatorCode: String,
    val operator: String,
    val admin: String,
    val matchId: String,
    val routeIdxFrom: Int? = null,
    val routeIdxTo: Int? = null
)

data class Icon(
    val res: String
)

data class OperatorInfo(
    val name: String,
    val nameS: String,
    val nameN: String,
    val nameL: String,
    val id: String
)
