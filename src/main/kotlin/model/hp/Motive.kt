package model.hp

import kotlinx.serialization.Serializable

@Serializable
data class Motive(
    var id: String = "",
    var label: String = "",
    var allowedLocation: String = "",
    var calendarConfigurations: List<CalendarConfigurations> = emptyList()
)