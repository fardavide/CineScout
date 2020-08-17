package client.cli

import assert4k.*
import client.cli.util.StringOutputStream
import client.cli.util.TestDispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import java.io.PrintStream
import kotlin.test.Test

internal class CliTest {

    private fun CoroutineScope.Cli() = Cli(this, TestDispatchersProvider())

    @Test
    fun `Menu is displayed on start`() = runBlockingTest {
        val cli = Cli()
        assert that cli.state equals State.Menu
        cli.clear()
    }

    @Test
    fun `Errors can be displayed correctly`() = runBlockingTest {
        val stringStream = StringOutputStream()
        System.setErr(PrintStream(stringStream))

        val cli = Cli()
        val command = "wrong command"
        cli execute command
        assert that stringStream.output equals "Cannot parse command '$command'"
        cli.clear()
    }
}
