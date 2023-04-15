package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseScreenplayId.TypeMovie
import cinescout.database.model.DatabaseScreenplayId.TypeTvShow
import cinescout.database.model.DatabaseScreenplayId.ValueSeparator
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbScreenplayId
import cinescout.database.model.DatabaseTmdbTvShowId

val TmdbScreenplayIdAdapter = object : ColumnAdapter<DatabaseTmdbScreenplayId, String> {

    override fun decode(databaseValue: String): DatabaseTmdbScreenplayId {
        val (type, id) = databaseValue.split(ValueSeparator)
        return when (type) {
            TypeMovie -> DatabaseTmdbMovieId(id.toInt())
            TypeTvShow -> DatabaseTmdbTvShowId(id.toInt())
            else -> error("Unknown type: $type")
        }
    }

    override fun encode(value: DatabaseTmdbScreenplayId) = when (value) {
        is DatabaseTmdbMovieId -> "$TypeMovie$ValueSeparator${value.value}"
        is DatabaseTmdbTvShowId -> "$TypeTvShow$ValueSeparator${value.value}"
    }
}
