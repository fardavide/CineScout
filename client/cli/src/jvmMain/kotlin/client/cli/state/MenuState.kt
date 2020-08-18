package client.cli.state

import client.cli.controller.MenuController
import client.cli.headerThemed
import client.cli.themed
import com.jakewharton.picnic.TextAlignment.MiddleCenter
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table

object MenuState : State<MenuController>(MenuController::class) {

    override fun render() = table {
        themed()

        header {
            headerThemed()

            row {
                cell("${cyan}Welcome to CineScout!$Reset") {
                    alignment = MiddleCenter
                    columnSpan = 4
                }
            }
        }
        row {
            cell("")
            cell("commands") {
                alignment = MiddleCenter
                columnSpan = 3
            }
        }
        for (action in actions) {
            row(action.description, *action.commands.toTypedArray())
        }
    }.renderText()
}
