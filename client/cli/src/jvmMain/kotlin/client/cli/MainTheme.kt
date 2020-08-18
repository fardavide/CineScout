package client.cli

import com.jakewharton.picnic.CellStyleDsl
import com.jakewharton.picnic.TableDsl
import com.jakewharton.picnic.TableSectionDsl
import entities.util.unsupported

var theme = MainTheme(
    header = object : Theme {
        override val border = true
        override val verticalPadding = 1
        override val horizontalPadding = 5
    },
    border = true,
    verticalPadding = 0,
    horizontalPadding = 3,
    colorized = false
)

data class MainTheme(
    val header: Theme,
    override val border: Boolean,
    override val verticalPadding: Int,
    override val horizontalPadding: Int,
    val colorized: Boolean
) : Theme

interface Theme {
    val border: Boolean
    val verticalPadding: Int
    val horizontalPadding: Int
}


fun TableDsl.themed() = cellStyle {
    border = theme.border
    verticalPadding = theme.verticalPadding
    horizontalPadding = theme.horizontalPadding
}

fun TableSectionDsl.headerThemed() = cellStyle {
    border = theme.header.border
    verticalPadding = theme.header.verticalPadding
    horizontalPadding = theme.header.horizontalPadding
}


var CellStyleDsl.horizontalPadding: Int
    get() = unsupported
    set(value) {
        paddingLeft = value
        paddingRight = value
    }

var CellStyleDsl.verticalPadding: Int
    get() = unsupported
    set(value) {
        paddingTop = value
        paddingBottom = value
    }
