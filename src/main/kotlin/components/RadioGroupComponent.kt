package components

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.InputType
import react.dom.html.ReactHTML
import react.useState

external interface RadioGroupProps : Props {
    var name: String
    var onChange: (value: Any) -> Unit
    var displayableValues: List<DisplayableValue>
    var selectedValue: Any
}

val RadioGroupComponent = FC<RadioGroupProps> { props ->
    ReactHTML.fieldset {
        css {
            margin = 0.px
            padding = 0.px
            border = 0.px
            display = Display.flex
            justifyContent = JustifyContent.flexStart
        }
        var selectedValue by useState(props.selectedValue)
        for (displayableValue in props.displayableValues) {
            ReactHTML.div {
                css {
                    border = Border(1.px, LineStyle.solid, Color("#b8afac"))
                    width = 100.pct
                }
                ReactHTML.label {
                    ReactHTML.input {
                        checked = selectedValue == displayableValue.value
                        onChange = {
                            props.onChange(displayableValue.value)
                            selectedValue = displayableValue.value
                        }
                        value = displayableValue.label
                        type = InputType.radio
                        name = props.name
                    }
                    +displayableValue.label
                }
            }
        }
    }
}