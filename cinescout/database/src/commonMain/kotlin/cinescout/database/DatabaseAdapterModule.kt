package cinescout.database

import cinescout.database.adapter.BookmarkAdapter
import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.DateTimeAdapter
import cinescout.database.adapter.DoubleAdapter
import cinescout.database.adapter.DurationAdapter
import cinescout.database.adapter.GenreSlugAdapter
import cinescout.database.adapter.GravatarHashAdapter
import cinescout.database.adapter.HistoryItemIdAdapter
import cinescout.database.adapter.IntDoubleAdapter
import cinescout.database.adapter.IntLongAdapter
import cinescout.database.adapter.ListFilterAdapter
import cinescout.database.adapter.ListSortingAdapter
import cinescout.database.adapter.ListTypeAdapter
import cinescout.database.adapter.SuggestionSourceAdapter
import cinescout.database.adapter.TmdbEpisodeIdAdapter
import cinescout.database.adapter.TmdbKeywordIdAdapter
import cinescout.database.adapter.TmdbMovieIdAdapter
import cinescout.database.adapter.TmdbPersonIdAdapter
import cinescout.database.adapter.TmdbScreenplayIdAdapter
import cinescout.database.adapter.TmdbSeasonIdAdapter
import cinescout.database.adapter.TmdbTvShowIdAdapter
import cinescout.database.adapter.TmdbVideoIdAdapter
import cinescout.database.adapter.TmdbVideoResolutionAdapter
import cinescout.database.adapter.TmdbVideoSiteAdapter
import cinescout.database.adapter.TmdbVideoTypeAdapter
import cinescout.database.adapter.TraktAccessTokenAdapter
import cinescout.database.adapter.TraktAccountUsernameAdapter
import cinescout.database.adapter.TraktAuthStateValueAdapter
import cinescout.database.adapter.TraktAuthorizationCodeAdapter
import cinescout.database.adapter.TraktEpisodeIdAdapter
import cinescout.database.adapter.TraktMovieIdAdapter
import cinescout.database.adapter.TraktRefreshTokenAdapter
import cinescout.database.adapter.TraktScreenplayIdAdapter
import cinescout.database.adapter.TraktSeasonIdAdapter
import cinescout.database.adapter.TraktTvShowIdAdapter
import cinescout.database.adapter.TvShowStatusAdapter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
class DatabaseAdapterModule {

    @Factory
    fun anticipatedAdapter() = Anticipated.Adapter(
        traktIdAdapter = TraktScreenplayIdAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )

    @Factory
    fun appSettingsAdapter() = AppSettings.Adapter(
        savedListFilterAdapter = ListFilterAdapter,
        savedListSortingAdapter = ListSortingAdapter,
        savedListTypeAdapter = ListTypeAdapter
    )

    @Factory
    fun episodeAdapter() = Episode.Adapter(
        firstAirDateAdapter = DateAdapter,
        numberAdapter = IntLongAdapter,
        runtimeAdapter = DurationAdapter,
        seasonIdAdapter = TraktSeasonIdAdapter,
        seasonNumberAdapter = IntLongAdapter,
        tmdbIdAdapter = TmdbEpisodeIdAdapter,
        traktIdAdapter = TraktEpisodeIdAdapter
    )

    @Factory
    fun fetchDataAdapter() = FetchData.Adapter(
        bookmarkAdapter = BookmarkAdapter,
        dateTimeAdapter = DateTimeAdapter
    )

    @Factory
    fun genreAdapter() = Genre.Adapter(slugAdapter = GenreSlugAdapter)

    @Factory
    fun historyAdapter() = History.Adapter(
        episodeNumberAdapter = IntLongAdapter,
        itemIdAdapter = HistoryItemIdAdapter,
        seasonNumberAdapter = IntLongAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter,
        watchedAtAdapter = DateTimeAdapter
    )

    @Factory
    fun keywordAdapter() = Keyword.Adapter(tmdbIdAdapter = TmdbKeywordIdAdapter)

    @Factory
    fun movieAdapter() = Movie.Adapter(
        releaseDateAdapter = DateAdapter,
        runtimeAdapter = DurationAdapter,
        tmdbIdAdapter = TmdbMovieIdAdapter,
        traktIdAdapter = TraktMovieIdAdapter
    )

    @Factory
    fun personAdapter() = Person.Adapter(tmdbIdAdapter = TmdbPersonIdAdapter)

    @Factory
    fun personalRatingAdapter() = PersonalRating.Adapter(
        ratingAdapter = IntDoubleAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )

    @Factory
    fun popularAdapter() = Popular.Adapter(
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )

    @Factory
    fun recommendationAdapter() = Recommendation.Adapter(
        screenplayTmdbIdAdapter = TmdbScreenplayIdAdapter,
        screenplayTraktIdAdapter = TraktScreenplayIdAdapter
    )

    @Factory
    fun recommendedAdapter() = Recommended.Adapter(
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
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
        genreSlugAdapter = GenreSlugAdapter,
        screenplayIdAdapter = TraktScreenplayIdAdapter
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
    fun seasonAdapter() = Season.Adapter(
        firstAirDateAdapter = DateAdapter,
        numberAdapter = IntLongAdapter,
        tmdbIdAdapter = TmdbSeasonIdAdapter,
        tmdbTvShowIdAdapter = TmdbTvShowIdAdapter,
        traktIdAdapter = TraktSeasonIdAdapter,
        traktTvShowIdAdapter = TraktTvShowIdAdapter
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
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
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
    fun trendingAdapter() = Trending.Adapter(
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )

    @Factory
    fun tvShowAdapter() = TvShow.Adapter(
        firstAirDateAdapter = DateAdapter,
        runtimeAdapter = DurationAdapter,
        statusAdapter = TvShowStatusAdapter,
        tmdbIdAdapter = TmdbTvShowIdAdapter,
        traktIdAdapter = TraktTvShowIdAdapter
    )

    @Factory
    fun votingAdapter() = Voting.Adapter(
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )

    @Factory
    fun watchlistAdapter() = Watchlist.Adapter(
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )
}
