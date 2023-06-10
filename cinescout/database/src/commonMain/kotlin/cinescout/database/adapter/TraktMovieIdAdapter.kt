package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseScreenplayId.TypeMovie
import cinescout.database.model.id.DatabaseScreenplayId.ValueSeparator
import cinescout.database.model.id.DatabaseTraktMovieId

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
