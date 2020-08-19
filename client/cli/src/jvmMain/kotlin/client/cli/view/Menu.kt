package client.cli.view

import client.cli.Action
import client.cli.themed
import com.jakewharton.picnic.TextAlignment.MiddleCenter
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table

class Menu(val actions: Set<Action>) : View {

    override fun render() = table {
        themed()

        Header(4, "${cyan}Welcome to CineScout!$Reset")

        row {
            cell("")
            cell("commands") {
                alignment = MiddleCenter
                columnSpan = 3
            }
        }
        ActionsView(actions)
    }.renderText()
}
