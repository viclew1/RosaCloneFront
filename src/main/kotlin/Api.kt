import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime
import model.ApiPaths
import model.availabilities.Availability
import model.hp.CalendarConfigurations
import model.hp.HealthProfessional
import model.hp.Motive

const val endpoint = "https://staging-api.rosa.be/api"

val jsonClient = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
    }
}

suspend fun getHealthProfessional(): HealthProfessional {
    return jsonClient.get(endpoint + ApiPaths.HP.path + "/antoine-staging-pairet")
}

suspend fun getAvailabilities(
    calendarConfiguration: CalendarConfigurations,
    motive: Motive,
    isNewPatient: Boolean,
    from: LocalDate,
    to: LocalDate
): List<Availability> {
    return jsonClient.get(
        endpoint + ApiPaths.AVAILABILITIES.path
                + "?from=" + from.atTime(LocalTime(0, 0))
                + "&to=" + to.atTime(LocalTime(23, 59))
                + "&motive_id=" + motive.id
                + "&is_new_patient=" + isNewPatient
                + "&calendar_ids=" + calendarConfiguration.calendarId
    )
}