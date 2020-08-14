package client.cli

import domain.SearchMovies
import entities.movies.Movie
import entities.util.equalsNoCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject

class Cli(scope: CoroutineScope) : CoroutineScope by scope, KoinComponent {


    private val searchMovies: SearchMovies by inject()

    init {
        launch {
            var currentState: GUI.State = GUI.State.Menu

            GUI(this, flow {
                emit(GUI.State.Menu)

                    while (true) {
                        print("command: ")
                        currentState = execute(currentState, readLine()!!)
                        emit(currentState)
                    }
            })
        }
    }

    private suspend fun execute(currentState: GUI.State, command: String): GUI.State {
        if (setOf("*esc", "*e").any { it equalsNoCase command }) {
            return GUI.State.Menu
        }

        when (currentState) {
            is GUI.State.Menu -> {
                command.check("1", "Search") { return GUI.State.Search.Input }
                command.check("2", "Rate") { return GUI.State.Rate }
                command.check("3", "Suggestion") { return GUI.State.Suggestion }
            }
            is GUI.State.Search.Input, is GUI.State.Search.Result -> {
                return GUI.State.Search.Result(searchMovies(command))
            }
            is GUI.State.Search.Loading -> {}
            is GUI.State.Rate -> {}
            is GUI.State.Suggestion -> {}
        }

        throw IllegalArgumentException("Cannot parse command: $command")
    }

    private inline fun String.check(vararg args: String, block: () -> Unit) {
        if (args.any { it equalsNoCase this }) block()
    }


    private fun Collection<Movie>.prettyPrint(): String {
        return joinToString(prefix = "\n", separator = "\n\n", postfix = "\n") { movie ->
            """
                ${movie.name.s}
                Tmdb id: ${movie.id.i}
                Year: ${movie.year}
                Actors: ${movie.actors.joinToString { it.name.s }}
                Genres: ${movie.genres.joinToString { it.name.s }}
            """.trimIndent()
        }
    }
}

suspend fun main(): Unit = coroutineScope {

    startKoin {
        modules(cliClientModule)
    }

    Cli(this)
}
