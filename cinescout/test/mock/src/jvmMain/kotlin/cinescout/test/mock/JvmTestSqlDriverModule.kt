package cinescout.test.mock

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.koin.dsl.module

internal actual val TestSqlDriverModule = module {

    single<SqlDriver> { JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY) }
}
