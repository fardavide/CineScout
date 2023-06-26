package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.id.DatabaseGenreSlug

val GenreSlugAdapter = object : ColumnAdapter<DatabaseGenreSlug, String> {

    override fun decode(databaseValue: String) = DatabaseGenreSlug(databaseValue)

    override fun encode(value: DatabaseGenreSlug) = value.value
}
