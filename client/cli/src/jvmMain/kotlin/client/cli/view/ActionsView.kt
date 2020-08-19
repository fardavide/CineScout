package client.cli.view

import client.cli.Action
import com.jakewharton.picnic.TableDsl

class ActionsView(val table: TableDsl, val actions: Set<Action>) : View {

    override fun render() = with(table) {
        for (action in actions) {
            row(action.description, *action.commands.toTypedArray())
        }
    }
}

fun TableDsl.ActionsView(actions: Set<Action>) =
    ActionsView(this, actions).render()
