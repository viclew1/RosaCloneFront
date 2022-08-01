package components

import csstype.*
import emotion.react.css
import getAvailabilities
import io.ktor.util.date.Month
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import model.availabilities.Availability
import model.hp.Motive
import react.FC
import react.Props
import react.dom.html.ButtonType
import react.dom.html.ReactHTML
import react.useEffectOnce
import react.useState

private const val MAX_DELTA_DATE = 5
private const val DISPLAYED_DATES_COUNT = 5

private val scope = MainScope()

external interface CalendarComponentProps : Props {
    var motive: Motive
    var isNewPatient: Boolean
}

private data class CalendarState(
    val dateDelta: Int,
    val displayedDates: List<LocalDate>,
    val availabilities: List<Availability>
)

val CalendarComponent = FC<CalendarComponentProps> { props ->
    var state by useState(CalendarState(0, getDisplayedDates(0), emptyList()))

    useEffectOnce {
        onDateDeltaUpdate(state, props, state.dateDelta) { state = it }
    }
    val availabilitiesByDate = state.availabilities
        .groupBy { it.startAt.toLocalDateTime(TimeZone.currentSystemDefault()).date }

    ReactHTML.table {
        css {
            width = 100.pct
        }
        ReactHTML.thead {
            ReactHTML.tr {
                ThButtonDeltaChangeComponent {
                    disabled = state.dateDelta <= 0
                    label = "<"
                    onClick = { onDateDeltaUpdate(state, props, state.dateDelta - 1) { state = it } }
                }
                for (date in state.displayedDates) {
                    ReactHTML.th {
                        ReactHTML.div {
                            css {
                                display = Display.flex
                                flexDirection = FlexDirection.column
                                justifyContent = JustifyContent.center
                                fontSize = 12.px
                            }
                            ReactHTML.span {
                                css {
                                    fontSize = 9.px
                                    lineHeight = 11.px
                                    textAlign = TextAlign.center
                                    textTransform = TextTransform.uppercase
                                    color = Color("#9c727d")
                                }
                                +date.dayOfWeek.name.substring(0, 2)
                            }
                            +"${Month.from(date.month.ordinal).value} ${date.dayOfMonth}"
                        }
                    }
                }
                ThButtonDeltaChangeComponent {
                    disabled = state.dateDelta >= MAX_DELTA_DATE
                    label = ">"
                    onClick = { onDateDeltaUpdate(state, props, state.dateDelta + 1) { state = it } }
                }
            }
        }
        ReactHTML.tbody {
            val rowCount = maxOf(3, availabilitiesByDate.maxOfOrNull { it.value.size } ?: 0)
            for (i in 0 until rowCount) {
                ReactHTML.tr {
                    ReactHTML.td {}
                    for (date in state.displayedDates) {
                        val availability = availabilitiesByDate[date]?.getOrNull(i)
                        val displayedValue = availability?.let {
                            val startAt = it.startAt.toLocalDateTime(TimeZone.currentSystemDefault())
                            val hourPart = startAt.hour.toString().padStart(2, '0')
                            val minPart = startAt.minute.toString().padStart(2, '0')
                            "$hourPart:$minPart"
                        } ?: "-"
                        ReactHTML.td {
                            ReactHTML.div {
                                ReactHTML.span {
                                    css {
                                        textAlign = TextAlign.center
                                        fontSize = 12.px
                                    }
                                    +displayedValue
                                }
                            }
                        }
                    }
                    ReactHTML.td {}
                }
            }
        }
    }
}

private fun onDateDeltaUpdate(
    currentState: CalendarState,
    props: CalendarComponentProps,
    newDateDelta: Int,
    updateState: (CalendarState) -> Unit
) {
    val newDisplayedDates = getDisplayedDates(newDateDelta)
    val newState = currentState.copy(dateDelta = newDateDelta, displayedDates = newDisplayedDates)
    updateState(newState)
    scope.launch {
        updateState(newState.copy(availabilities = getNewAvailabilities(props, newDisplayedDates)))
    }
}

private suspend fun getNewAvailabilities(
    props: CalendarComponentProps,
    displayedDates: List<LocalDate>
): List<Availability> {
    return getAvailabilities(
        props.motive.calendarConfigurations[0],
        props.motive,
        props.isNewPatient,
        displayedDates.first(),
        displayedDates.last()
    )
}

private fun getDisplayedDates(dateDelta: Int): List<LocalDate> {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return Array(DISPLAYED_DATES_COUNT) {
        currentDate.plus(dateDelta * DISPLAYED_DATES_COUNT + it, DateTimeUnit.DAY)
    }.toList()
}

private external interface ThButtonDeltaChangeProps : Props {
    var disabled: Boolean
    var label: String
    var onClick: () -> Unit
}

private val ThButtonDeltaChangeComponent = FC<ThButtonDeltaChangeProps> { props ->
    ReactHTML.th {
        css {
            width = 65.px
        }
        ReactHTML.button {
            type = ButtonType.button
            disabled = props.disabled
            onClick = { props.onClick() }
            +props.label
        }
    }
}