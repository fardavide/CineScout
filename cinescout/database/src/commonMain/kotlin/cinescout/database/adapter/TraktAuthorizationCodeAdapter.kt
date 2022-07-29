package cinescout.database.adapter

import app.cash.sqldelight.ColumnAdapter
import cinescout.database.model.DatabaseTraktAuthorizationCode

val TraktAuthorizationCodeAdapter = object : ColumnAdapter<DatabaseTraktAuthorizationCode, String> {
    override fun decode(databaseValue: String) = DatabaseTraktAuthorizationCode(databaseValue)

    override fun encode(value: DatabaseTraktAuthorizationCode) = value.value
}
