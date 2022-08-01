package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTraktAccountUsername

val TraktAccountUsernameAdapter = object : ColumnAdapter<DatabaseTraktAccountUsername, String> {
    override fun decode(databaseValue: String) = DatabaseTraktAccountUsername(databaseValue)

    override fun encode(value: DatabaseTraktAccountUsername) = value.value
}
