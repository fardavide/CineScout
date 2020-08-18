package client.cli.controller

import client.cli.HomeAction
import client.cli.state.AnyState
import client.cli.state.MenuState

class RateMovieController : Controller() {

    override val actions = setOf(
        HomeAction
    )

    override infix fun execute(command: String): AnyState {
        return when (actionBy(command)) {
            HomeAction -> MenuState
            else -> TODO("Should parse the input and rate the Movies")
        }
    }
}
