package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbAccountId

val TmdbAccountIdAdapter = object : ColumnAdapter<DatabaseTmdbAccountId, String> {

    override fun decode(databaseValue: String) = DatabaseTmdbAccountId(databaseValue)

    override fun encode(value: DatabaseTmdbAccountId) = value.value
}
