package client.cli.view

import client.cli.headerThemed
import com.jakewharton.picnic.TableDsl
import com.jakewharton.picnic.TextAlignment.MiddleCenter

class Header(val table: TableDsl, private val columnSpan: Int, val title: String) : View {

    override fun render() = with(table) {
        header {
            headerThemed()

            row {
                cell(title) {
                    alignment = MiddleCenter
                    columnSpan = this@Header.columnSpan
                }
            }
        }
    }
}

fun TableDsl.Header(columnSpan: Int, title: String) =
    Header(this, columnSpan, title).render()
