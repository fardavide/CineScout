package client.cli.view

import client.cli.Action
import client.cli.themed
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import entities.movies.Movie

class SearchResult(private val movies: Collection<Movie>, val actions: Set<Action>) : View {

    override fun render() = table {
        themed()

        for (movie in movies) {
            MovieView(movie)
        }

        ActionsView(2, actions)

    }.renderText()
}
