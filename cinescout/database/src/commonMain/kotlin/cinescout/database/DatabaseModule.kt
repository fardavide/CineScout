package cinescout.database

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.SqlDriver
import org.koin.core.module.Module
import org.koin.dsl.module

val DatabaseModule = module {

    includes(DatabaseAdapterModule)
    includes(DatabaseQueriesModule)
    includes(SqlDriverModule)

    factory<Transacter> { get<Database>() }
    single {
        val driver: SqlDriver = get()
        Database(
            driver = driver,
            genreAdapter = get(),
            keywordAdapter = get(),
            likedMovieAdapter = get(),
            movieAdapter = get(),
            movieCastMemberAdapter = get(),
            movieCrewMemberAdapter = get(),
            movieGenreAdapter = get(),
            movieKeywordAdapter = get(),
            movieRatingAdapter = get(),
            personAdapter = get(),
            storeFetchDataAdapter = get(),
            suggestedMovieAdapter = get(),
            tmdbAccountAdapter = get(),
            tmdbAuthStateAdapter = get(),
            traktAuthStateAdapter = get(),
            traktAccountAdapter = get(),
            watchlistAdapter = get()
        )
    }
}

internal expect val SqlDriverModule: Module
