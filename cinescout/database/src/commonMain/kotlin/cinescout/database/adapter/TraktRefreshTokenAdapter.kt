package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTraktRefreshToken

val TraktRefreshTokenAdapter = object : ColumnAdapter<DatabaseTraktRefreshToken, String> {
    override fun decode(databaseValue: String) = DatabaseTraktRefreshToken(databaseValue)

    override fun encode(value: DatabaseTraktRefreshToken) = value.value
}
