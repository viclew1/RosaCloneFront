package model.availabilities

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class Availability(
    @Serializable(with = InstantIso8601Serializer::class) val startAt: Instant,
    @Serializable(with = InstantIso8601Serializer::class) val endAt: Instant,
    val duration: Int
)