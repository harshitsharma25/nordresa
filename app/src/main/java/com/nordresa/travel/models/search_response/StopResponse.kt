package com.nordresa.travel.models.search_response

data class StopResponse(
    val stopLocationOrCoordLocation: List<StopLocationOrCoordLocation>,
    val TechnicalMessages: TechnicalMessages?,
    val serverVersion: String?,
    val dialectVersion: String?,
    val requestId: String?
)

data class StopLocationOrCoordLocation(
    val StopLocation: StopLocation?
)

data class StopLocation(
    val productAtStop: List<ProductAtStop>?,
    val timezoneOffset: Int?,
    val id: String?,
    val extId: String?,
    val name: String?,
    val lon: Double?,
    val lat: Double?,
    val weight: Int?,
    val products: Int?,
    val minimumChangeDuration: String?
)

data class ProductAtStop(
    val icon: Icon?,
    val cls: String?
)

data class Icon(
    val res: String?
)

data class TechnicalMessages(
    val TechnicalMessage: List<TechnicalMessage>?
)

data class TechnicalMessage(
    val key: String?,
    val value: String?
)
