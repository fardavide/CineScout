package cinescout.database

import org.koin.core.scope.Scope
import org.koin.dsl.module

val DatabaseQueriesModule = module {

    factory { database.movieQueries }
}

private val Scope.database get() = get<Database>()

