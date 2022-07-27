package cinescout.database

import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.RatingAdapter
import cinescout.database.adapter.TmdbAccessTokenAdapter
import cinescout.database.adapter.TmdbAccountIdAdapter
import cinescout.database.adapter.TmdbAuthStateValueAdapter
import cinescout.database.adapter.TmdbIdAdapter
import cinescout.database.adapter.TmdbRequestTokenAdapter
import cinescout.database.adapter.TmdbSessionIdAdapter
import cinescout.database.adapter.TraktAccessTokenAdapter
import cinescout.database.adapter.TraktRefreshTokenAdapter
import org.koin.dsl.module

val DatabaseAdapterModule = module {

    factory { Movie.Adapter(releaseDateAdapter = DateAdapter, tmdbIdAdapter = TmdbIdAdapter) }
    factory { MovieRating.Adapter(tmdbIdAdapter = TmdbIdAdapter, ratingAdapter = RatingAdapter) }
    factory {
        TmdbAuthState.Adapter(
            accessTokenAdapter = TmdbAccessTokenAdapter,
            accountIdAdapter = TmdbAccountIdAdapter,
            requestTokenAdapter = TmdbRequestTokenAdapter,
            sessionIdAdapter = TmdbSessionIdAdapter,
            stateAdapter = TmdbAuthStateValueAdapter
        )
    }
    factory {
        TraktTokens.Adapter(
            accessTokenAdapter = TraktAccessTokenAdapter,
            refreshTokenAdapter = TraktRefreshTokenAdapter
        )
    }
    factory { Watchlist.Adapter(tmdbIdAdapter = TmdbIdAdapter) }
}
