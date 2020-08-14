package database

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual val DatabaseDriver: SqlDriver = JdbcSqliteDriver(DATABASE_FILE_NAME)
