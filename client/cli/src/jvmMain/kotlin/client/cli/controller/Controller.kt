package client.cli.controller

import client.cli.Action
import client.cli.error.throwWrongCommand
import client.cli.state.State
import entities.util.equalsNoCase

abstract class Controller {

    abstract infix fun execute(command: String): State<*>

    abstract val actions: Set<Action>

    protected fun actionBy(command: String) =
        actions.find { action -> action.commands.any { it equalsNoCase command } }
            ?: command.throwWrongCommand()
}
