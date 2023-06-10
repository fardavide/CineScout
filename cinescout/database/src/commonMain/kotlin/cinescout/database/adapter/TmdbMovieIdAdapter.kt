package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseScreenplayId.TypeMovie
import cinescout.database.model.id.DatabaseScreenplayId.ValueSeparator
import cinescout.database.model.id.DatabaseTmdbMovieId

val TmdbMovieIdAdapter = object : ColumnAdapter<DatabaseTmdbMovieId, String> {

    override fun decode(databaseValue: String): DatabaseTmdbMovieId {
        val (type, id) = databaseValue.split(ValueSeparator)
        return when (type) {
            TypeMovie -> DatabaseTmdbMovieId(id.toInt())
            else -> error("Unknown type: $type")
        }
    }

    override fun encode(value: DatabaseTmdbMovieId) = "$TypeMovie$ValueSeparator${value.value}"
}
