package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseScreenplayId.TypeMovie
import cinescout.database.model.DatabaseScreenplayId.ValueSeparator
import cinescout.database.model.DatabaseTraktMovieId

val TraktMovieIdAdapter = object : ColumnAdapter<DatabaseTraktMovieId, String> {

    override fun decode(databaseValue: String): DatabaseTraktMovieId {
        val (type, id) = databaseValue.split(ValueSeparator)
        return when (type) {
            TypeMovie -> DatabaseTraktMovieId(id.toInt())
            else -> error("Unknown type: $type")
        }
    }

    override fun encode(value: DatabaseTraktMovieId) = "$TypeMovie$ValueSeparator${value.value}"
}
