package client.cli.controller

import client.cli.Action
import client.cli.state.State
import entities.util.equalsNoCase

abstract class Controller {

    abstract suspend infix fun execute(command: String): State<*>

    abstract val actions: Set<Action>

    protected fun actionBy(command: String) =
        actions.find { action -> action.commands.any { it equalsNoCase command } }
            ?: Action("other")
}
