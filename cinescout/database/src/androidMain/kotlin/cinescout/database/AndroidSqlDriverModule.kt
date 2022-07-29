package cinescout.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.dsl.module

actual val SqlDriverModule = module {

    single<SqlDriver> { AndroidSqliteDriver(context = get(), schema = Database.Schema, name = "cinescout.db") }
}
