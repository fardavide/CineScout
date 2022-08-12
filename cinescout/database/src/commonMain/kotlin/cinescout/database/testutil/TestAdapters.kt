package cinescout.database.testutil

import cinescout.database.Genre
import cinescout.database.Keyword
import cinescout.database.LikedMovie
import cinescout.database.Movie
import cinescout.database.MovieCastMember
import cinescout.database.MovieCrewMember
import cinescout.database.MovieGenre
import cinescout.database.MovieKeyword
import cinescout.database.MovieRating
import cinescout.database.Person
import cinescout.database.SuggestedMovie
import cinescout.database.TmdbAccount
import cinescout.database.TmdbAuthState
import cinescout.database.TraktAccount
import cinescout.database.TraktAuthState
import cinescout.database.Watchlist
import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.DoubleAdapter
import cinescout.database.adapter.GravatarHashAdapter
import cinescout.database.adapter.TmdbAccessTokenAdapter
import cinescout.database.adapter.TmdbAccountIdAdapter
import cinescout.database.adapter.TmdbAccountUsernameAdapter
import cinescout.database.adapter.TmdbAuthStateValueAdapter
import cinescout.database.adapter.TmdbGenreIdAdapter
import cinescout.database.adapter.TmdbKeywordIdAdapter
import cinescout.database.adapter.TmdbMovieIdAdapter
import cinescout.database.adapter.TmdbPersonIdAdapter
import cinescout.database.adapter.TmdbRequestTokenAdapter
import cinescout.database.adapter.TmdbSessionIdAdapter
import cinescout.database.adapter.TraktAccessTokenAdapter
import cinescout.database.adapter.TraktAccountUsernameAdapter
import cinescout.database.adapter.TraktAuthStateValueAdapter
import cinescout.database.adapter.TraktAuthorizationCodeAdapter
import cinescout.database.adapter.TraktRefreshTokenAdapter

object TestAdapters {

    val GenreAdapter = Genre.Adapter(tmdbIdAdapter = TmdbGenreIdAdapter)
    val KeywordAdapter = Keyword.Adapter(tmdbIdAdapter = TmdbKeywordIdAdapter)
    val LikedMovieAdapter = LikedMovie.Adapter(tmdbIdAdapter = TmdbMovieIdAdapter)
    val MovieCastMemberAdapter = MovieCastMember.Adapter(
        movieIdAdapter = TmdbMovieIdAdapter,
        personIdAdapter = TmdbPersonIdAdapter
    )
    val MovieAdapter = Movie.Adapter(
        releaseDateAdapter = DateAdapter,
        tmdbIdAdapter = TmdbMovieIdAdapter
    )
    val MovieCrewMemberAdapter = MovieCrewMember.Adapter(
        movieIdAdapter = TmdbMovieIdAdapter,
        personIdAdapter = TmdbPersonIdAdapter
    )
    val MovieGenreAdapter = MovieGenre.Adapter(genreIdAdapter = TmdbGenreIdAdapter, movieIdAdapter = TmdbMovieIdAdapter)
    val MovieKeywordAdapter = MovieKeyword.Adapter(
        keywordIdAdapter = TmdbKeywordIdAdapter,
        movieIdAdapter = TmdbMovieIdAdapter
    )
    val MovieRatingAdapter = MovieRating.Adapter(tmdbIdAdapter = TmdbMovieIdAdapter, ratingAdapter = DoubleAdapter)
    val PersonAdapter = Person.Adapter(tmdbIdAdapter = TmdbPersonIdAdapter)
    val SuggestedMovieAdapter = SuggestedMovie.Adapter(
        affinityAdapter = DoubleAdapter,
        tmdbIdAdapter = TmdbMovieIdAdapter
    )
    val TmdbAccountAdapter = TmdbAccount.Adapter(
        gravatarHashAdapter = GravatarHashAdapter,
        usernameAdapter = TmdbAccountUsernameAdapter
    )
    val TmdbAuthStateAdapter = TmdbAuthState.Adapter(
        accessTokenAdapter = TmdbAccessTokenAdapter,
        accountIdAdapter = TmdbAccountIdAdapter,
        requestTokenAdapter = TmdbRequestTokenAdapter,
        sessionIdAdapter = TmdbSessionIdAdapter,
        stateAdapter = TmdbAuthStateValueAdapter
    )
    val TraktAccountAdapter = TraktAccount.Adapter(
        gravatarHashAdapter = GravatarHashAdapter,
        usernameAdapter = TraktAccountUsernameAdapter
    )
    val TraktAuthStateAdapter = TraktAuthState.Adapter(
        accessTokenAdapter = TraktAccessTokenAdapter,
        authorizationCodeAdapter = TraktAuthorizationCodeAdapter,
        refreshTokenAdapter = TraktRefreshTokenAdapter,
        stateAdapter = TraktAuthStateValueAdapter
    )
    val WatchlistAdapter = Watchlist.Adapter(tmdbIdAdapter = TmdbMovieIdAdapter)
}
