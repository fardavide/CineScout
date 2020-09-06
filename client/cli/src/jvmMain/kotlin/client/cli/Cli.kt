package client.cli

import client.cli.state.MenuState
import client.cli.state.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import util.DispatchersProvider
import java.lang.System.err

suspend fun main(): Unit = coroutineScope {

    val koin = startKoin {
        modules(cliClientModule)
    }.koin

    val cli = Cli(this, dispatchers = koin.get())
    while (true) {
        cli execute readLine()!!
        print("\n\n\n")
    }
}

class Cli(
    private val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
): CoroutineScope by scope, DispatchersProvider by dispatchers {

    lateinit var state: State
        private set

    private val renderer = scope.actor<State> {
        for (s in channel) {
            state = s
            print(state.render())
            print("command: ")
        }
    }

    init {
        renderer.sendBlocking(MenuState)
    }

    infix fun execute(command: String) {
        scope.launch(Comp) {
            try {
                renderer.send(state execute command)

            } catch (error: Throwable) {
                err.println(error.message)
            }
        }
    }

    fun clear() {
        renderer.close()
    }
}
