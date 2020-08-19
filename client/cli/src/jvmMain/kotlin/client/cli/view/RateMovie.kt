package client.cli.view

import client.cli.Action
import client.cli.themed
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table

class RateMovie(val actions: Set<Action>) : View {

    override fun render() = table {
        themed()

        Header(3, "${cyan}Insert the TMDB id of the Movie that you want to rate positively${Reset}")

        ActionsView(actions)

    }.renderText()
}
