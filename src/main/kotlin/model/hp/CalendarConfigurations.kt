package model.hp

import kotlinx.serialization.Serializable

@Serializable
data class CalendarConfigurations(
    var calendarId: String = ""
)