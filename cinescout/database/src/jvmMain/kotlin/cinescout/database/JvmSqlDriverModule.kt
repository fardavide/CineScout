package cinescout.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.dsl.module

actual val SqlDriverModule = module {

    single<SqlDriver> { JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY) }
}
