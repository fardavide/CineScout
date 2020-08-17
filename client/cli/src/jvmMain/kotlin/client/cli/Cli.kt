package client.cli

import client.DispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import java.lang.System.err

suspend fun main(): Unit = coroutineScope {

    startKoin {
        modules(cliClientModule)
    }

    val cli = Cli(this, object : DispatchersProvider {
        override val Main = Dispatchers.Default
        override val Comp = Dispatchers.Default
        override val Io = Dispatchers.IO
    })
    while (true) {
        cli execute readLine()!!
    }
}

class Cli(
    private val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
): CoroutineScope by scope, DispatchersProvider by dispatchers {

    var state: State = State.Menu
        private set

    private val renderer = scope.actor<State> {
        for (s in channel) {
            state = s
        }
    }

    infix fun execute(command: String) {
        scope.launch(Comp) {
            try {
                renderer.send(state parse command)

            } catch (error: Throwable) {
                err.println(error.message)
            }
        }
    }

    fun clear() {
        renderer.close()
    }
}
