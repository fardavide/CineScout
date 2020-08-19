package client.cli.view

import client.cli.Action
import client.cli.themed
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table

class SearchView(val actions: Set<Action>) : View {

    override fun render() = table {
        themed()

        Header(3, "Insert the tile or part of it, for the Movie that you want to search")

        ActionsView(actions)

    }.renderText()
}
