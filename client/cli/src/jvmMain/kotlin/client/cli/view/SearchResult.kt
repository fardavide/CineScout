package client.cli.view

import client.cli.Action
import client.cli.themed
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import entities.movies.Movie
import entities.util.ellipseAt

class SearchResult(private val movies: Collection<Movie>, val actions: Set<Action>) : View {

    override fun render() = table {
        themed()

        // TODO move to MovieView
        for (movie in movies) {
            row {
                cell("ID: ${movie.id.i}")
                cell(movie.name.s) {
                    columnSpan = 8
                }
                cell(movie.year)
            }
            if (movie.actors.isNotEmpty()) {
                row {
                    cell("Cast")
                    cell(movie.actors.joinToString { it.name.s }.ellipseAt(120)) {
                        columnSpan = 9
                    }
                }
            }
            if (movie.genres.isNotEmpty()) {
                row {
                    cell("Genres")
                    cell(movie.genres.joinToString { it.name.s }) {
                        columnSpan = 9
                    }
                }
            }
            row()
        }

        for (action in actions) {
            row {
                cell(action.description) {
                    columnSpan = 2
                }
                cells(*action.commands.toTypedArray())
            }
        }
    }.renderText()
}
