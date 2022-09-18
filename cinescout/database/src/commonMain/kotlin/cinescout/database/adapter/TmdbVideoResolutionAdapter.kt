package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseVideoResolution

val TmdbVideoResolutionAdapter = object : ColumnAdapter<DatabaseVideoResolution, String> {

    override fun decode(databaseValue: String) = DatabaseVideoResolution.values().first { it.name == databaseValue }

    override fun encode(value: DatabaseVideoResolution) = value.name
}
