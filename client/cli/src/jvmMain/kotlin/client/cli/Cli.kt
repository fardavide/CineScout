package client.cli

import client.DispatchersProvider
import client.cli.state.AnyState
import client.cli.state.MenuState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.lang.System.err

suspend fun main(): Unit = coroutineScope {

    val dispatchers = object : DispatchersProvider {
        override val Main = Dispatchers.Default
        override val Comp = Dispatchers.Default
        override val Io = Dispatchers.IO
    }

    val scopeModule = module {
        factory<CoroutineScope> { this@coroutineScope }
        factory<DispatchersProvider> { dispatchers }
    }

    startKoin {
        modules(cliClientModule + scopeModule)
    }

    val cli = Cli(this, dispatchers)
    while (true) {
        cli execute readLine()!!
        print("\n\n\n")
    }
}

class Cli(
    private val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
): CoroutineScope by scope, DispatchersProvider by dispatchers {

    lateinit var state: AnyState
        private set

    private val renderer = scope.actor<AnyState> {
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
