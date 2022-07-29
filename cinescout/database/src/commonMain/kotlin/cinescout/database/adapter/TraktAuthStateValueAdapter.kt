package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTraktAuthStateValue

val TraktAuthStateValueAdapter = object : ColumnAdapter<DatabaseTraktAuthStateValue, String> {
    override fun decode(databaseValue: String): DatabaseTraktAuthStateValue =
        DatabaseTraktAuthStateValue.values().first { it.name.lowercase() == databaseValue }

    override fun encode(value: DatabaseTraktAuthStateValue) = value.name.lowercase()
}
