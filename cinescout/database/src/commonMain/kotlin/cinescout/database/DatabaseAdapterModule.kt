package cinescout.database

import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.RatingAdapter
import cinescout.database.adapter.TmdbAccessTokenAdapter
import cinescout.database.adapter.TmdbAccountIdAdapter
import cinescout.database.adapter.TmdbIdAdapter
import cinescout.database.adapter.TmdbSessionIdAdapter
import org.koin.dsl.module

val DatabaseAdapterModule = module {

    factory { Movie.Adapter(releaseDateAdapter = DateAdapter, tmdbIdAdapter = TmdbIdAdapter) }
    factory { MovieRating.Adapter(tmdbIdAdapter = TmdbIdAdapter, ratingAdapter = RatingAdapter) }
    factory {
        TmdbCredentials.Adapter(
            accessTokenAdapter = TmdbAccessTokenAdapter,
            accountIdAdapter = TmdbAccountIdAdapter,
            sessionIdAdapter = TmdbSessionIdAdapter
        )
    }
    factory { Watchlist.Adapter(tmdbIdAdapter = TmdbIdAdapter) }
}
