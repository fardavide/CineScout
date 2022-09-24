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
            name = DATABASE_NAME
        )
        driver.also {
            get<Context>().deleteDatabase(DATABASE_NAME)
            Database.Schema.create(driver)
        }
    }
}

private const val DATABASE_NAME = "cinescout-test.db"
