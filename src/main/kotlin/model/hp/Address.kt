package model.hp

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    @SerialName("_id") var id: String = "",
    var street: String = "",
    var number: String = "",
    var zipCode: String = "",
    var city: String = "",
    var country: String = "",
)