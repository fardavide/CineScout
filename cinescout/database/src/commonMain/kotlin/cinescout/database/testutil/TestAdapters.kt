package cinescout.database.testutil

import cinescout.database.Genre
import cinescout.database.Keyword
import cinescout.database.Movie
import cinescout.database.MovieBackdrop
import cinescout.database.MovieCastMember
import cinescout.database.MovieCrewMember
import cinescout.database.MovieGenre
import cinescout.database.MovieKeyword
import cinescout.database.MoviePoster
import cinescout.database.MovieVideo
import cinescout.database.Person
import cinescout.database.PersonalRating
import cinescout.database.Recommendation
import cinescout.database.Similar
import cinescout.database.StoreFetchData
import cinescout.database.Suggestion
import cinescout.database.TraktAccount
import cinescout.database.TraktAuthState
import cinescout.database.TvShow
import cinescout.database.TvShowBackdrop
import cinescout.database.TvShowCastMember
import cinescout.database.TvShowCrewMember
import cinescout.database.TvShowGenre
import cinescout.database.TvShowKeyword
import cinescout.database.TvShowPoster
import cinescout.database.TvShowVideo
import cinescout.database.Voting
import cinescout.database.Watchlist
import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.DateTimeAdapter
import cinescout.database.adapter.DoubleAdapter
import cinescout.database.adapter.GravatarHashAdapter
import cinescout.database.adapter.IntAdapter
import cinescout.database.adapter.SuggestionSourceAdapter
import cinescout.database.adapter.TmdbGenreIdAdapter
import cinescout.database.adapter.TmdbKeywordIdAdapter
import cinescout.database.adapter.TmdbMovieIdAdapter
import cinescout.database.adapter.TmdbPersonIdAdapter
import cinescout.database.adapter.TmdbScreenplayIdAdapter
import cinescout.database.adapter.TmdbTvShowIdAdapter
import cinescout.database.adapter.TmdbVideoIdAdapter
import cinescout.database.adapter.TmdbVideoResolutionAdapter
import cinescout.database.adapter.TmdbVideoSiteAdapter
import cinescout.database.adapter.TmdbVideoTypeAdapter
import cinescout.database.adapter.TraktAccessTokenAdapter
import cinescout.database.adapter.TraktAccountUsernameAdapter
import cinescout.database.adapter.TraktAuthStateValueAdapter
import cinescout.database.adapter.TraktAuthorizationCodeAdapter
import cinescout.database.adapter.TraktRefreshTokenAdapter

object TestAdapters {

    val GenreAdapter = Genre.Adapter(tmdbIdAdapter = TmdbGenreIdAdapter)
    val KeywordAdapter = Keyword.Adapter(tmdbIdAdapter = TmdbKeywordIdAdapter)
    val MovieCastMemberAdapter = MovieCastMember.Adapter(
        movieIdAdapter = TmdbMovieIdAdapter,
        personIdAdapter = TmdbPersonIdAdapter
    )
    val MovieAdapter = Movie.Adapter(
        releaseDateAdapter = DateAdapter,
        tmdbIdAdapter = TmdbMovieIdAdapter
    )
    val MovieBackdropAdapter = MovieBackdrop.Adapter(movieIdAdapter = TmdbMovieIdAdapter)
    val MovieCrewMemberAdapter = MovieCrewMember.Adapter(
        movieIdAdapter = TmdbMovieIdAdapter,
        personIdAdapter = TmdbPersonIdAdapter
    )
    val MovieGenreAdapter = MovieGenre.Adapter(genreIdAdapter = TmdbGenreIdAdapter, movieIdAdapter = TmdbMovieIdAdapter)
    val MovieKeywordAdapter = MovieKeyword.Adapter(
        keywordIdAdapter = TmdbKeywordIdAdapter,
        movieIdAdapter = TmdbMovieIdAdapter
    )
    val MoviePosterAdapter = MoviePoster.Adapter(movieIdAdapter = TmdbMovieIdAdapter)
    val MovieVideoAdapter = MovieVideo.Adapter(
        movieIdAdapter = TmdbMovieIdAdapter,
        idAdapter = TmdbVideoIdAdapter,
        resolutionAdapter = TmdbVideoResolutionAdapter,
        siteAdapter = TmdbVideoSiteAdapter,
        typeAdapter = TmdbVideoTypeAdapter
    )
    val PersonAdapter = Person.Adapter(tmdbIdAdapter = TmdbPersonIdAdapter)
    val PersonalRatingAdapter = PersonalRating.Adapter(
        ratingAdapter = IntAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )
    val RecommendationAdapter = Recommendation.Adapter(
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )
    val SimilarAdapter = Similar.Adapter(
        similarTmdbIdAdapter = TmdbScreenplayIdAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )
    val StoreFetchDataAdapter = StoreFetchData.Adapter(dateTimeAdapter = DateTimeAdapter)
    val SuggestionAdapter = Suggestion.Adapter(
        affinityAdapter = DoubleAdapter,
        sourceAdapter = SuggestionSourceAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
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
    val TvShowAdapter = TvShow.Adapter(
        firstAirDateAdapter = DateAdapter,
        tmdbIdAdapter = TmdbTvShowIdAdapter
    )
    val TvShowBackdropAdapter = TvShowBackdrop.Adapter(tvShowIdAdapter = TmdbTvShowIdAdapter)
    val TvShowCastMemberAdapter = TvShowCastMember.Adapter(
        personIdAdapter = TmdbPersonIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )
    val TvShowCrewMemberAdapter = TvShowCrewMember.Adapter(
        personIdAdapter = TmdbPersonIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )
    val TvShowGenreAdapter = TvShowGenre.Adapter(
        genreIdAdapter = TmdbGenreIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )
    val TvShowKeywordAdapter = TvShowKeyword.Adapter(
        keywordIdAdapter = TmdbKeywordIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )
    val TvShowPosterAdapter = TvShowPoster.Adapter(tvShowIdAdapter = TmdbTvShowIdAdapter)
    val TvShowVideoAdapter = TvShowVideo.Adapter(
        idAdapter = TmdbVideoIdAdapter,
        resolutionAdapter = TmdbVideoResolutionAdapter,
        siteAdapter = TmdbVideoSiteAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter,
        typeAdapter = TmdbVideoTypeAdapter
    )
    val VotingAdapter = Voting.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)
    val WatchlistAdapter = Watchlist.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)
}
