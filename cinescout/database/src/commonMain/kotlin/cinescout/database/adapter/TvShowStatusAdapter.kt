package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTvShowStatus

val TvShowStatusAdapter = object : ColumnAdapter<DatabaseTvShowStatus, String> {

    override fun decode(databaseValue: String) = DatabaseTvShowStatus.fromName(databaseValue)

    override fun encode(value: DatabaseTvShowStatus) = value.name
}
