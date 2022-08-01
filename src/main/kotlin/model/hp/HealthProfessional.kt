package model.hp

import kotlinx.serialization.Serializable

@Serializable
data class HealthProfessional(
    var id: String = "",
    var key: String = "",
    var title: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var nihii: Long = 0,
    var motives: List<Motive> = emptyList(),
    var sites: List<Site> = emptyList()
)