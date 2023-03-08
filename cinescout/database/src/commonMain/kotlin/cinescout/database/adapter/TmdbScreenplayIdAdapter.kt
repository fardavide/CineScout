package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTmdbScreenplayId

val TmdbScreenplayIdAdapter = object : ColumnAdapter<DatabaseTmdbScreenplayId, String> {

    override fun decode(databaseValue: String): DatabaseTmdbScreenplayId {
        val (type, id) = databaseValue.split(ValueSeparator)
        return when (type) {
            TypeMovie -> DatabaseTmdbScreenplayId.Movie(id.toInt())
            TypeTvShow -> DatabaseTmdbScreenplayId.TvShow(id.toInt())
            else -> error("Unknown type: $type")
        }
    }

    override fun encode(value: DatabaseTmdbScreenplayId) = when (value) {
        is DatabaseTmdbScreenplayId.Movie -> "$TypeMovie$ValueSeparator${value.value}"
        is DatabaseTmdbScreenplayId.TvShow -> "$TypeTvShow$ValueSeparator${value.value}"
    }
}

private const val TypeMovie = "movie"
private const val TypeTvShow = "tv_show"
private const val ValueSeparator = "_"

