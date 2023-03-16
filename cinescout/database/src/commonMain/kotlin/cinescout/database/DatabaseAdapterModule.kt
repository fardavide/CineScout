package cinescout.database

import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.DateTimeAdapter
import cinescout.database.adapter.DoubleAdapter
import cinescout.database.adapter.GravatarHashAdapter
import cinescout.database.adapter.IntDoubleAdapter
import cinescout.database.adapter.IntLongAdapter
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
    fun fetchDataAdapter() =
        FetchData.Adapter(dateTimeAdapter = DateTimeAdapter, pageAdapter = IntLongAdapter)

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
    fun personAdapter() = Person.Adapter(tmdbIdAdapter = TmdbPersonIdAdapter)

    @Factory
    fun personalRatingAdapter() = PersonalRating.Adapter(
        ratingAdapter = IntDoubleAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun recommendationAdapter() = Recommendation.Adapter(
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun screenplayBackdropAdapter() =
        ScreenplayBackdrop.Adapter(screenplayIdAdapter = TmdbScreenplayIdAdapter)

    @Factory
    fun screenplayCastMemberAdapter() = ScreenplayCastMember.Adapter(
        personIdAdapter = TmdbPersonIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun screenplayCrewMemberAdapter() = ScreenplayCrewMember.Adapter(
        personIdAdapter = TmdbPersonIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun screenplayGenreAdapter() = ScreenplayGenre.Adapter(
        genreIdAdapter = TmdbGenreIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun screenplayKeywordAdapter() = ScreenplayKeyword.Adapter(
        keywordIdAdapter = TmdbKeywordIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun screenplayPosterAdapter() = ScreenplayPoster.Adapter(screenplayIdAdapter = TmdbScreenplayIdAdapter)

    @Factory
    fun screenplayVideoAdapter() = ScreenplayVideo.Adapter(
        idAdapter = TmdbVideoIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter,
        resolutionAdapter = TmdbVideoResolutionAdapter,
        siteAdapter = TmdbVideoSiteAdapter,
        typeAdapter = TmdbVideoTypeAdapter
    )

    @Factory
    fun similarAdapter() = Similar.Adapter(
        similarTmdbIdAdapter = TmdbScreenplayIdAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )

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
    fun votingAdapter() = Voting.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)

    @Factory
    fun watchlistAdapter() = Watchlist.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)
}
