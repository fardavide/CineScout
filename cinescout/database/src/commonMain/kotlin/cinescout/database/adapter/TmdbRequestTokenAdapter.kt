package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbRequestToken

val TmdbRequestTokenAdapter = object : ColumnAdapter<DatabaseTmdbRequestToken, String> {
    override fun decode(databaseValue: String) = DatabaseTmdbRequestToken(databaseValue)

    override fun encode(value: DatabaseTmdbRequestToken) = value.value
}
