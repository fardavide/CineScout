package client.cli

import domain.SearchMovies
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
        if (command.check("*esc", "*e")) {
            return GUI.State.Menu
        }

        return when (currentState) {
            is GUI.State.Menu -> {
                command.check("1", "Search") { GUI.State.Search.Input } ?:
                command.check("2", "Rate") { GUI.State.Rate } ?:
                command.check("3", "Suggestion") { GUI.State.Suggestion }
                    .orFail(command)
            }
            is GUI.State.Search.Input, is GUI.State.Search.Result -> {
                GUI.State.Search.Result(searchMovies(command))
            }
            is GUI.State.Search.Loading -> { null.orFail(command) }
            is GUI.State.Rate -> { null.orFail(command) }
            is GUI.State.Suggestion -> { null.orFail(command) }
        }

    }

    private inline fun <T> String.check(vararg args: String, block: () -> T): T? {
        return if (check(*args)) block()
        else null
    }

    private fun String.check(vararg args: String): Boolean =
        args.any { it equalsNoCase this }

    @Suppress("unused")
    private fun <T : GUI.State> T?.orFail(command: String): T =
        throw IllegalArgumentException("Cannot parse command: $command")
}

suspend fun main(): Unit = coroutineScope {

    startKoin {
        modules(cliClientModule)
    }

    Cli(this)
}
