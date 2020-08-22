package client.cli.view

import client.cli.Action
import client.cli.themed
import com.jakewharton.picnic.TextAlignment.MiddleCenter
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import entities.movies.Movie

class Suggestion(private val movie: Movie, val actions: Set<Action>) : View {

    override fun render() = table {
        themed()

        MovieView(movie)

        row {
            cell("${cyan}Do you like this movie?${Reset}") {
                alignment = MiddleCenter
                columnSpan = 3
            }
        }

        ActionsView(actions)

    }.renderText()
}
