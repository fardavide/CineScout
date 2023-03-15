package cinescout.database

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
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class DatabaseAdapterModule {

    @Factory
    fun genreAdapter() = Genre.Adapter(tmdbIdAdapter = TmdbGenreIdAdapter)

    @Factory
    fun keywordAdapter() = Keyword.Adapter(tmdbIdAdapter = TmdbKeywordIdAdapter)

    @Factory
    fun movieAdapter() = Movie.Adapter(
        releaseDateAdapter = DateAdapter,
        tmdbIdAdapter = TmdbMovieIdAdapter
    )

    @Factory
    fun movieBackdropAdapter() = MovieBackdrop.Adapter(movieIdAdapter = TmdbMovieIdAdapter)

    @Factory
    fun movieCastMemberAdapter() = MovieCastMember.Adapter(
        movieIdAdapter = TmdbMovieIdAdapter,
        personIdAdapter = TmdbPersonIdAdapter
    )

    @Factory
    fun movieCrewMemberAdapter() = MovieCrewMember.Adapter(
        movieIdAdapter = TmdbMovieIdAdapter,
        personIdAdapter = TmdbPersonIdAdapter
    )

    @Factory
    fun movieGenreAdapter() = MovieGenre.Adapter(
        genreIdAdapter = TmdbGenreIdAdapter,
        movieIdAdapter = TmdbMovieIdAdapter
    )

    @Factory
    fun movieKeywordAdapter() = MovieKeyword.Adapter(
        keywordIdAdapter = TmdbKeywordIdAdapter,
        movieIdAdapter = TmdbMovieIdAdapter
    )

    @Factory
    fun moviePosterAdapter() = MoviePoster.Adapter(movieIdAdapter = TmdbMovieIdAdapter)

    @Factory
    fun movieRecommendationAdapter() = MovieRecommendation.Adapter(
        movieIdAdapter = TmdbMovieIdAdapter,
        recommendedMovieIdAdapter = TmdbMovieIdAdapter
    )

    @Factory
    fun movieVideoAdapter() = MovieVideo.Adapter(
        idAdapter = TmdbVideoIdAdapter,
        movieIdAdapter = TmdbMovieIdAdapter,
        resolutionAdapter = TmdbVideoResolutionAdapter,
        siteAdapter = TmdbVideoSiteAdapter,
        typeAdapter = TmdbVideoTypeAdapter
    )

    @Factory
    fun personAdapter() = Person.Adapter(tmdbIdAdapter = TmdbPersonIdAdapter)

    @Factory
    fun personalRatingAdapter() = PersonalRating.Adapter(
        ratingAdapter = IntAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun recommendationAdapter() = Recommendation.Adapter(
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun storeFetchDataAdapter() = StoreFetchData.Adapter(dateTimeAdapter = DateTimeAdapter)

    @Factory
    fun suggestionAdapter() = Suggestion.Adapter(
        affinityAdapter = DoubleAdapter,
        sourceAdapter = SuggestionSourceAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun traktAccountAdapter() = TraktAccount.Adapter(
        gravatarHashAdapter = GravatarHashAdapter,
        usernameAdapter = TraktAccountUsernameAdapter
    )

    @Factory
    fun traktAuthStateAdapter() = TraktAuthState.Adapter(
        accessTokenAdapter = TraktAccessTokenAdapter,
        authorizationCodeAdapter = TraktAuthorizationCodeAdapter,
        refreshTokenAdapter = TraktRefreshTokenAdapter,
        stateAdapter = TraktAuthStateValueAdapter
    )

    @Factory
    fun tvShowAdapter() = TvShow.Adapter(
        firstAirDateAdapter = DateAdapter,
        tmdbIdAdapter = TmdbTvShowIdAdapter
    )

    @Factory
    fun tvShowBackdropAdapter() = TvShowBackdrop.Adapter(tvShowIdAdapter = TmdbTvShowIdAdapter)

    @Factory
    fun tvShowCastMemberAdapter() = TvShowCastMember.Adapter(
        personIdAdapter = TmdbPersonIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )

    @Factory
    fun tvShowCrewMemberAdapter() = TvShowCrewMember.Adapter(
        personIdAdapter = TmdbPersonIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )

    @Factory
    fun tvShowGenreAdapter() = TvShowGenre.Adapter(
        genreIdAdapter = TmdbGenreIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )

    @Factory
    fun tvShowKeywordAdapter() = TvShowKeyword.Adapter(
        keywordIdAdapter = TmdbKeywordIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )

    @Factory
    fun tvShowPosterAdapter() = TvShowPoster.Adapter(tvShowIdAdapter = TmdbTvShowIdAdapter)

    @Factory
    fun tvShowRecommendationAdapter() = TvShowRecommendation.Adapter(
        recommendedTvShowIdAdapter = TmdbTvShowIdAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter
    )

    @Factory
    fun tvShowVideoAdapter() = TvShowVideo.Adapter(
        idAdapter = TmdbVideoIdAdapter,
        resolutionAdapter = TmdbVideoResolutionAdapter,
        siteAdapter = TmdbVideoSiteAdapter,
        tvShowIdAdapter = TmdbTvShowIdAdapter,
        typeAdapter = TmdbVideoTypeAdapter
    )

    @Factory
    fun votingAdapter() = Voting.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)

    @Factory
    fun watchlistAdapter() = Watchlist.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)
}
