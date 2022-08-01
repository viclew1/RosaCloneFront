package model.hp

import kotlinx.serialization.Serializable

@Serializable
data class Site(
    var id: String = "",
    var name: String = "",
    var address: Address = Address()
)