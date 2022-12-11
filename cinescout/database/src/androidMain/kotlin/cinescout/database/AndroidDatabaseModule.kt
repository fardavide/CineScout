package cinescout.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.koin.java.KoinJavaComponent

internal actual val Driver: SqlDriver = AndroidSqliteDriver(
    schema = Database.Schema,
    context = KoinJavaComponent.getKoin().get(),
    name = "cinescout.db",
    cacheSize = 50
)
