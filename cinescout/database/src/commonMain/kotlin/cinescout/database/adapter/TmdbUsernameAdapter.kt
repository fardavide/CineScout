package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbAccountUsername

val TmdbAccountUsernameAdapter = object : ColumnAdapter<DatabaseTmdbAccountUsername, String> {
    override fun decode(databaseValue: String) = DatabaseTmdbAccountUsername(databaseValue)

    override fun encode(value: DatabaseTmdbAccountUsername) = value.value
}
