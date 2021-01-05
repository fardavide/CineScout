package client.cli.state

import client.cli.Action
import client.cli.Palette
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import util.equalsNoCase

@OptIn(KoinApiExtension::class)
abstract class State : Palette, KoinComponent {

    abstract val actions: Set<Action>

    abstract suspend infix fun execute(command: String): State

    abstract fun render(): String

    protected fun actionBy(command: String) =
        actions.find { action -> action.commands.any { it equalsNoCase command } }
            ?: Action("other")


    companion object

    interface GetActions {

        val actions: Set<Action>
    }
}
