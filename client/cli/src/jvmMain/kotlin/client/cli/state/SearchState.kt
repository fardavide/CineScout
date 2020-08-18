package client.cli.state

import client.cli.controller.SearchController
import client.cli.headerThemed
import client.cli.themed
import com.jakewharton.picnic.TextAlignment.MiddleCenter
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table

class SearchState : State<SearchController>(SearchController::class) {

    override fun render() = table {
        themed()

        header {
            headerThemed()

            row {
                cell("Insert the tile or part of it, for the Movie that you want to search") {
                    alignment = MiddleCenter
                    columnSpan = 3
                }
            }
        }

        for (action in actions) {
            row(action.description, *action.commands.toTypedArray())
        }
    }.renderText()
}
