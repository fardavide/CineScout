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
            movieBackdropAdapter = get(),
            movieCastMemberAdapter = get(),
            movieCrewMemberAdapter = get(),
            movieGenreAdapter = get(),
            movieKeywordAdapter = get(),
            moviePosterAdapter = get(),
            movieRatingAdapter = get(),
            movieRecommendationAdapter = get(),
            movieVideoAdapter = get(),
            personAdapter = get(),
            storeFetchDataAdapter = get(),
            suggestedMovieAdapter = get(),
            tmdbAccountAdapter = get(),
            tmdbAuthStateAdapter = get(),
            traktAuthStateAdapter = get(),
            traktAccountAdapter = get(),
            tvShowAdapter = get(),
            tvShowBackdropAdapter = get(),
            tvShowCastMemberAdapter = get(),
            tvShowCrewMemberAdapter = get(),
            tvShowGenreAdapter = get(),
            tvShowKeywordAdapter = get(),
            tvShowPosterAdapter = get(),
            tvShowRatingAdapter = get(),
            tvShowVideoAdapter = get(),
            tvShowWatchlistAdapter = get(),
            watchlistAdapter = get()
        )
    }
}

internal expect val SqlDriverModule: Module
