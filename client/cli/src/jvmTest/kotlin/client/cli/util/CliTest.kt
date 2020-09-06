package client.cli.util

import client.cli.cliClientModule
import client.viewModel.GetSuggestedMovieViewModel
import client.viewModel.RateMovieViewModel
import client.viewModel.SearchViewModel
import io.mockk.mockk
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.KoinTestRule
import util.test.CoroutinesTest
import java.io.PrintStream

abstract class CliTest : AutoCloseKoinTest(), CoroutinesTest {

    @get:Rule val cliRule = CliTestRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            cliClientModule +
            module(override = true) {
                single<GetSuggestedMovieViewModel> { mockk() }
                single<RateMovieViewModel> { mockk(relaxed = true) }
                single<SearchViewModel> { mockk() }
            }
        )
    }
}

class CliTestRule : TestRule {

    override fun apply(base: Statement, description: Description) = base.apply {
        System.setOut(PrintStream(StringOutputStream()))
    }
}
