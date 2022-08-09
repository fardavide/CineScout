package cinescout.database

import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.GravatarHashAdapter
import cinescout.database.adapter.RatingAdapter
import cinescout.database.adapter.TmdbAccessTokenAdapter
import cinescout.database.adapter.TmdbAccountIdAdapter
import cinescout.database.adapter.TmdbAccountUsernameAdapter
import cinescout.database.adapter.TmdbAuthStateValueAdapter
import cinescout.database.adapter.TmdbGenreIdAdapter
import cinescout.database.adapter.TmdbMovieIdAdapter
import cinescout.database.adapter.TmdbPersonIdAdapter
import cinescout.database.adapter.TmdbRequestTokenAdapter
import cinescout.database.adapter.TmdbSessionIdAdapter
import cinescout.database.adapter.TraktAccessTokenAdapter
import cinescout.database.adapter.TraktAccountUsernameAdapter
import cinescout.database.adapter.TraktAuthStateValueAdapter
import cinescout.database.adapter.TraktAuthorizationCodeAdapter
import cinescout.database.adapter.TraktRefreshTokenAdapter
import org.koin.dsl.module

val DatabaseAdapterModule = module {

    factory { Genre.Adapter(tmdbIdAdapter = TmdbGenreIdAdapter) }
    factory { LikedMovie.Adapter(tmdbIdAdapter = TmdbMovieIdAdapter) }
    factory { Movie.Adapter(releaseDateAdapter = DateAdapter, tmdbIdAdapter = TmdbMovieIdAdapter) }
    factory { MovieCastMember.Adapter(movieIdAdapter = TmdbMovieIdAdapter, personIdAdapter = TmdbPersonIdAdapter) }
    factory { MovieCrewMember.Adapter(movieIdAdapter = TmdbMovieIdAdapter, personIdAdapter = TmdbPersonIdAdapter) }
    factory { MovieGenre.Adapter(genreIdAdapter = TmdbGenreIdAdapter, movieIdAdapter = TmdbMovieIdAdapter) }
    factory { MovieRating.Adapter(tmdbIdAdapter = TmdbMovieIdAdapter, ratingAdapter = RatingAdapter) }
    factory { Person.Adapter(tmdbIdAdapter = TmdbPersonIdAdapter) }
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
        TraktAccount.Adapter(
            gravatarHashAdapter = GravatarHashAdapter,
            usernameAdapter = TraktAccountUsernameAdapter
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
    factory { Watchlist.Adapter(tmdbIdAdapter = TmdbMovieIdAdapter) }
}
