package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseVideoType

val TmdbVideoTypeAdapter = object : ColumnAdapter<DatabaseVideoType, String> {

    override fun decode(databaseValue: String) = DatabaseVideoType.values().first { it.name == databaseValue }

    override fun encode(value: DatabaseVideoType) = value.name
}
