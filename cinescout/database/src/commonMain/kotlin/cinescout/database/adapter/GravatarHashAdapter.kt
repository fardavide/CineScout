package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseGravatarHash

val GravatarHashAdapter = object : ColumnAdapter<DatabaseGravatarHash, String> {
    override fun decode(databaseValue: String) = DatabaseGravatarHash(databaseValue)

    override fun encode(value: DatabaseGravatarHash) = value.value
}
