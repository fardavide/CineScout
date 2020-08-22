package client.cli.view

import client.cli.Action
import com.jakewharton.picnic.TableDsl

class ActionsView(val table: TableDsl, private val descriptionColumnSpan: Int = 1, val actions: Set<Action>) : View {

    override fun render() = with(table) {
        for (action in actions) {
            row {
                cell(action.description) {
                    columnSpan = descriptionColumnSpan
                }
                cells(*action.commands.toTypedArray())
            }
        }
    }
}

fun TableDsl.ActionsView(actions: Set<Action>) =
    ActionsView(this, actions = actions, ).render()

fun TableDsl.ActionsView(descriptionColumnSpan: Int, actions: Set<Action>) =
    ActionsView(this, descriptionColumnSpan, actions, ).render()
