package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseVideoSite

val TmdbVideoSiteAdapter = object : ColumnAdapter<DatabaseVideoSite, String> {

    override fun decode(databaseValue: String) = DatabaseVideoSite.values().first { it.name == databaseValue }

    override fun encode(value: DatabaseVideoSite) = value.name
}
