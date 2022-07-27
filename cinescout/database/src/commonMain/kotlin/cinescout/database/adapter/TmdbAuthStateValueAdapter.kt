package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbAuthStateValue

val TmdbAuthStateValueAdapter = object : ColumnAdapter<DatabaseTmdbAuthStateValue, String> {
    override fun decode(databaseValue: String): DatabaseTmdbAuthStateValue =
        DatabaseTmdbAuthStateValue.values().first { it.name.lowercase() == databaseValue }

    override fun encode(value: DatabaseTmdbAuthStateValue) = value.name.lowercase()
}
