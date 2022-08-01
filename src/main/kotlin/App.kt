import components.CalendarComponent
import components.DisplayableValue
import components.DropDownListComponent
import components.RadioGroupComponent
import csstype.px
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import model.hp.HealthProfessional
import model.hp.Motive
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useEffectOnce
import react.useState

private val scope = MainScope()

private data class AppState(
    val healthProfessional: HealthProfessional,
    val motive: Motive,
    val isNewPatient: Boolean,
    val loading: Boolean
)

val App = FC<Props> {
    var state by useState(
        AppState(
            HealthProfessional(),
            Motive(),
            isNewPatient = true,
            loading = true
        )
    )

    useEffectOnce {
        scope.launch {
            val hp = getHealthProfessional()
            state = state.copy(
                healthProfessional = hp,
                motive = hp.motives[0],
                loading = false
            )
        }
    }

    ReactHTML.div {
        css {
            width = 350.px
            maxWidth = 350.px
            minWidth = 350.px
        }
        if (state.loading) {
            ReactHTML.h4 { +"Loading ..." }
        } else {
            val healthProfessional = state.healthProfessional
            ReactHTML.h4 { +"Find availability" }
            ReactHTML.p { +"Is this your first appointment with this practitioner?" }
            RadioGroupComponent {
                name = "isFirstAppointment"
                displayableValues = listOf(
                    DisplayableValue("Yes", true),
                    DisplayableValue("No", false)
                )
                selectedValue = state.isNewPatient
                onChange = { value -> state = state.copy(isNewPatient = value as Boolean) }
            }

            ReactHTML.p { +"What is the reason for your visit?" }
            DropDownListComponent {
                name = "motive"
                displayableValues = healthProfessional.motives.map {
                    DisplayableValue(it.label, it)
                }
                selectedValue = state.motive
                onChange = { value -> state = state.copy(motive = value as Motive) }
            }

            CalendarComponent {
                this.motive = state.motive
                this.isNewPatient = state.isNewPatient
            }
        }
    }
}