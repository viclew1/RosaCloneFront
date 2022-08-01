package components

import csstype.pct
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface DropDownListProps : Props {
    var name: String
    var onChange: (value: Any) -> Unit
    var displayableValues: List<DisplayableValue>
    var selectedValue: Any
}

val DropDownListComponent = FC<DropDownListProps> { props ->
    ReactHTML.select {
        css {
            width = 100.pct
        }
        name = props.name
        for (displayableValue in props.displayableValues) {
            ReactHTML.option {
                onSelect = {
                    props.onChange(displayableValue.value)
                }
                value = displayableValue.label
                +displayableValue.label
            }
        }
    }
}