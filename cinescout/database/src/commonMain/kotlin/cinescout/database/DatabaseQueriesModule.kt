package cinescout.database

import org.koin.core.scope.Scope
import org.koin.dsl.module

val DatabaseQueriesModule = module {

    factory { database.movieQueries }
    factory { database.movieRatingQueries }
    factory { database.tmdbCredentialsQueries }
    factory { database.watchlistQueries }
}

private val Scope.database get() = get<Database>()
