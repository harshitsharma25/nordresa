package com.nordresa.travel.models.route_planner

data class RoutePlannerResponse(
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

data class Trip(
    val Origin: StopLocation,
    val Destination: StopLocation,
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

data class StopLocation(
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

data class LegList(
    val Leg: List<Leg>
)

data class Leg(
    val Origin: StopLocation,
    val Destination: StopLocation,
    val Notes: Notes?,
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
    val icon: Icon?,
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

data class TripStatus(
    val hintCode: Int
)

data class TechnicalMessages(
    val TechnicalMessage: List<TechnicalMessage>
)

data class TechnicalMessage(
    val value: String,
    val key: String
)

data class ResultStatus(
    val timeDiffCritical: Boolean
)
