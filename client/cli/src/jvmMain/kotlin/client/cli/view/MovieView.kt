package client.cli.view

import com.jakewharton.picnic.TableDsl
import entities.movies.Movie
import entities.util.ellipseAt

class MovieView(val table: TableDsl, val movie: Movie) : View {

    override fun render() = with(table) {
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
}

fun TableDsl.MovieView(movie: Movie) =
    MovieView(this, movie).render()
