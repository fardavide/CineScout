package cinescout.database

import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.dsl.module

actual val SqlDriverModule = module {

    single<SqlDriver> {
        val driver = AndroidSqliteDriver(
            context = get(),
            schema = Database.Schema,
            name = "cinescout.db",
            cacheSize = 50,
            callback = object : AndroidSqliteDriver.Callback(Database.Schema) {

                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    setPragma(db, "JOURNAL_MODE = WAL")
                    setPragma(db, "SYNCHRONOUS = 2")
                }

                private fun setPragma(db: SupportSQLiteDatabase, pragma: String) {
                    val cursor = db.query("PRAGMA $pragma")
                    cursor.close()
                }
            }
        )
        driver.also { Database.Schema.create(driver) }
    }
}
