package cinescout.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

internal actual val Driver: SqlDriver =
    JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
