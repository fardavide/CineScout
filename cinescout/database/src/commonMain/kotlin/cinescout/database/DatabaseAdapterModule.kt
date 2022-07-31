package cinescout.database

import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.GravatarHashAdapter
import cinescout.database.adapter.RatingAdapter
import cinescout.database.adapter.TmdbAccessTokenAdapter
import cinescout.database.adapter.TmdbAccountIdAdapter
import cinescout.database.adapter.TmdbAccountUsernameAdapter
import cinescout.database.adapter.TmdbAuthStateValueAdapter
import cinescout.database.adapter.TmdbIdAdapter
import cinescout.database.adapter.TmdbRequestTokenAdapter
import cinescout.database.adapter.TmdbSessionIdAdapter
import cinescout.database.adapter.TraktAccessTokenAdapter
import cinescout.database.adapter.TraktAuthStateValueAdapter
import cinescout.database.adapter.TraktAuthorizationCodeAdapter
import cinescout.database.adapter.TraktRefreshTokenAdapter
import org.koin.dsl.module

val DatabaseAdapterModule = module {

    factory { Movie.Adapter(releaseDateAdapter = DateAdapter, tmdbIdAdapter = TmdbIdAdapter) }
    factory { MovieRating.Adapter(tmdbIdAdapter = TmdbIdAdapter, ratingAdapter = RatingAdapter) }
    factory {
        TmdbAccount.Adapter(
            gravatarHashAdapter = GravatarHashAdapter,
            usernameAdapter = TmdbAccountUsernameAdapter
        )
    }
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
        TraktAuthState.Adapter(
            authorizationCodeAdapter = TraktAuthorizationCodeAdapter,
            accessTokenAdapter = TraktAccessTokenAdapter,
            refreshTokenAdapter = TraktRefreshTokenAdapter,
            stateAdapter = TraktAuthStateValueAdapter
        )
    }
    factory { Watchlist.Adapter(tmdbIdAdapter = TmdbIdAdapter) }
}
