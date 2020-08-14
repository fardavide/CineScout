package client.cli

import com.jakewharton.picnic.Table
import com.jakewharton.picnic.TableDsl
import com.jakewharton.picnic.table
import entities.movies.Movie
import entities.util.ellipseAt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class GUI(scope: CoroutineScope, state: Flow<State>): CoroutineScope by scope {

    init {
        launch {
            state.collect {
                println(it.draw())
                println()
            }
        }
    }

    sealed class State {
        abstract fun draw(): Table

        object Menu : State() {
            override fun draw() = defaultTable {
                row {
                    cell("1. Search")
                    cell("2. Rate")
                    cell("3. Suggestion")
                }
            }
        }

        sealed class Search : State() {
            object Input : Search() {
                override fun draw() = defaultTable {
                    row {
                        cell("Search movie by title")
                        cell("Type '*esc' or '*e' for go back to the main menu")
                    }
                }
            }

            object Loading : State() {
                override fun draw(): Table {
                    TODO("Not yet implemented")
                }
            }

            class Result(private val movies: Collection<Movie>) : Search() {
                override fun draw() = defaultTable {
                    for (movie in movies) {
                        row { cellStyle { border = false } }
                        row {
                            cell(movie.name.s)
                            cell("Year ${movie.year}")
                            cell("Tmdb id: ${movie.id.i}")
                        }
                        row {
                            val content = "Actors: ${movie.actors.joinToString { it.name.s }}".ellipseAt(100)
                            cell(content) {
                                columnSpan = 3
                            }
                        }
                        row {
                            cell("Genres: ${movie.genres.joinToString { it.name.s }}") {
                                columnSpan = 3
                            }
                        }
                    }
                }
            }
        }
        object Rate : State() {
            override fun draw(): Table {
                TODO("Not yet implemented")
            }
        }

        object Suggestion : State() {
            override fun draw(): Table {
                TODO("Not yet implemented")
            }
        }

        private companion object {

            fun defaultTable(f: TableDsl.() -> Unit) = table {
                cellStyle {
                    border = true
                    paddingLeft = 2
                    paddingRight = 2
                }
                f()
            }

        }
    }
}
