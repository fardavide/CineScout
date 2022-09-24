package cinescout.test.mock

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import cinescout.database.Database
import org.koin.dsl.module

internal actual val TestSqlDriverModule = module {

    single<SqlDriver> {
        val driver = AndroidSqliteDriver(
            context = get(),
            schema = Database.Schema,
            name = "cinescout-test.db"
        )
        driver.also {
            get<Context>().deleteDatabase("cinescout-test.db")
            Database.Schema.create(driver)
        }
    }
}
