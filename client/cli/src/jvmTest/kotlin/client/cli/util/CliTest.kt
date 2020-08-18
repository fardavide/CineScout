package client.cli.util

import client.cli.cliClientModule
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.context.startKoin
import java.io.PrintStream

interface CliTest {
    @get:Rule val cliRule get() = CliTestRule
}

object CliTestRule : TestRule {

    private var hasStarted = false

    override fun apply(base: Statement, description: Description) = base.apply {
        System.setOut(PrintStream(StringOutputStream()))
        if (!hasStarted) {
            startKoin {
                modules(cliClientModule)
            }
            hasStarted = true
        }
    }
}
