package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseHistoryItemId

val HistoryItemIdAdapter = object : ColumnAdapter<DatabaseHistoryItemId, Long> {

    override fun decode(databaseValue: Long) = DatabaseHistoryItemId(databaseValue)

    override fun encode(value: DatabaseHistoryItemId) = value.value
}
