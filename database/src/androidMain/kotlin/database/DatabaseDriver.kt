package database

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.KoinComponent
import org.koin.core.get

private val contextGetter = object : KoinComponent {}

actual val DatabaseDriver: SqlDriver = AndroidSqliteDriver(Database.Schema, contextGetter.get(), "cinescout.db")
