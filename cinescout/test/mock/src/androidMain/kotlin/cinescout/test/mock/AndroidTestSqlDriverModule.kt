package cinescout.test.mock

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import cinescout.database.Database
import org.koin.dsl.module
import kotlin.random.Random

internal actual val TestSqlDriverModule get() = module {

    single<SqlDriver> {
        AndroidSqliteDriver(
            context = get(),
            schema = Database.Schema,
            name = DatabaseName
        ).also {
            get<Context>().deleteDatabase(DatabaseName)
            Database.Schema.create(it)
        }
    }
}

private val DatabaseName = "cinescout-test${Random.nextInt()}.db"
