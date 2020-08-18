package client.cli.controller

import client.cli.Action
import client.cli.HomeAction
import client.cli.state.AnyState
import client.cli.state.MenuState

class GetSuggestionsController : Controller() {

    private val YesAction = Action("Yes", "yes", "y")
    private val NoAction = Action("No", "no", "n")
    private val SkipAction = Action("Skip", "skip", "s")

    override val actions = setOf(
        YesAction,
        NoAction,
        SkipAction,
        HomeAction
    )

    override suspend infix fun execute(command: String): AnyState {
        return when (actionBy(command)) {
            HomeAction -> MenuState
            else -> TODO("Should deal with like, dislike, skip")
        }
    }
}
