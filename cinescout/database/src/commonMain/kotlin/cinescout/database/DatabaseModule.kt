package cinescout.database

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module

val DatabaseModule = module {

    includes(DatabaseAdapterModule)
    includes(DatabaseQueriesModule)
    includes(SqlDriverModule)

    single {
        val driver: SqlDriver = get()
        Database(
            driver = driver,
            movieAdapter = get(),
            movieRatingAdapter = get(),
            tmdbAuthStateAdapter = get(),
            traktTokensAdapter = get(),
            watchlistAdapter = get()
        )
    }
}

internal expect val SqlDriverModule: Module
