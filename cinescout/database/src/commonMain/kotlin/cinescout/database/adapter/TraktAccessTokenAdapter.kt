package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTraktAccessToken

val TraktAccessTokenAdapter = object : ColumnAdapter<DatabaseTraktAccessToken, String> {
    override fun decode(databaseValue: String) = DatabaseTraktAccessToken(databaseValue)

    override fun encode(value: DatabaseTraktAccessToken) = value.value
}
