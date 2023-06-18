package cinescout.database.testutil

import cinescout.database.Anticipated
import cinescout.database.AppSettings
import cinescout.database.Episode
import cinescout.database.FetchData
import cinescout.database.Genre
import cinescout.database.History
import cinescout.database.Keyword
import cinescout.database.Movie
import cinescout.database.Person
import cinescout.database.PersonalRating
import cinescout.database.Popular
import cinescout.database.Recommendation
import cinescout.database.Recommended
import cinescout.database.ScreenplayBackdrop
import cinescout.database.ScreenplayCastMember
import cinescout.database.ScreenplayCrewMember
import cinescout.database.ScreenplayGenre
import cinescout.database.ScreenplayKeyword
import cinescout.database.ScreenplayPoster
import cinescout.database.ScreenplayVideo
import cinescout.database.Season
import cinescout.database.Similar
import cinescout.database.Suggestion
import cinescout.database.TraktAccount
import cinescout.database.TraktAuthState
import cinescout.database.Trending
import cinescout.database.TvShow
import cinescout.database.Voting
import cinescout.database.Watchlist
import cinescout.database.adapter.DateAdapter
import cinescout.database.adapter.DateTimeAdapter
import cinescout.database.adapter.DoubleAdapter
import cinescout.database.adapter.DurationAdapter
import cinescout.database.adapter.GravatarHashAdapter
import cinescout.database.adapter.HistoryItemIdAdapter
import cinescout.database.adapter.IntDoubleAdapter
import cinescout.database.adapter.IntLongAdapter
import cinescout.database.adapter.ListFilterAdapter
import cinescout.database.adapter.ListSortingAdapter
import cinescout.database.adapter.ListTypeAdapter
import cinescout.database.adapter.SuggestionSourceAdapter
import cinescout.database.adapter.TmdbEpisodeIdAdapter
import cinescout.database.adapter.TmdbGenreIdAdapter
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

object TestAdapters {

    val AnticipatedAdapter = Anticipated.Adapter(
        traktIdAdapter = TraktScreenplayIdAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )
    val AppSettingsAdapter = AppSettings.Adapter(
        savedListFilterAdapter = ListFilterAdapter,
        savedListSortingAdapter = ListSortingAdapter,
        savedListTypeAdapter = ListTypeAdapter
    )
    val EpisodeAdapter = Episode.Adapter(
        firstAirDateAdapter = DateAdapter,
        numberAdapter = IntLongAdapter,
        runtimeAdapter = DurationAdapter,
        seasonIdAdapter = TraktSeasonIdAdapter,
        seasonNumberAdapter = IntLongAdapter,
        tmdbIdAdapter = TmdbEpisodeIdAdapter,
        traktIdAdapter = TraktEpisodeIdAdapter
    )
    val FetchDataAdapter = FetchData.Adapter(dateTimeAdapter = DateTimeAdapter, pageAdapter = IntLongAdapter)
    val GenreAdapter = Genre.Adapter(tmdbIdAdapter = TmdbGenreIdAdapter)
    val HistoryAdapter = History.Adapter(
        episodeNumberAdapter = IntLongAdapter,
        itemIdAdapter = HistoryItemIdAdapter,
        seasonNumberAdapter = IntLongAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter,
        watchedAtAdapter = DateTimeAdapter
    )
    val KeywordAdapter = Keyword.Adapter(tmdbIdAdapter = TmdbKeywordIdAdapter)
    val MovieAdapter = Movie.Adapter(
        releaseDateAdapter = DateAdapter,
        runtimeAdapter = DurationAdapter,
        tmdbIdAdapter = TmdbMovieIdAdapter,
        traktIdAdapter = TraktMovieIdAdapter
    )
    val PersonAdapter = Person.Adapter(tmdbIdAdapter = TmdbPersonIdAdapter)
    val PersonalRatingAdapter = PersonalRating.Adapter(
        ratingAdapter = IntDoubleAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )
    val PopularAdapter = Popular.Adapter(
        traktIdAdapter = TraktScreenplayIdAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )
    val RecommendationAdapter = Recommendation.Adapter(
        screenplayTmdbIdAdapter = TmdbScreenplayIdAdapter,
        screenplayTraktIdAdapter = TraktScreenplayIdAdapter
    )
    val RecommendedAdapter = Recommended.Adapter(
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )
    val ScreenplayBackdropAdapter = ScreenplayBackdrop.Adapter(
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )
    val ScreenplayCastMemberAdapter = ScreenplayCastMember.Adapter(
        personIdAdapter = TmdbPersonIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )
    val ScreenplayCrewMemberAdapter = ScreenplayCrewMember.Adapter(
        personIdAdapter = TmdbPersonIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )
    val ScreenplayGenreAdapter = ScreenplayGenre.Adapter(
        genreIdAdapter = TmdbGenreIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )
    val ScreenplayKeywordAdapter = ScreenplayKeyword.Adapter(
        keywordIdAdapter = TmdbKeywordIdAdapter,
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )
    val ScreenplayPosterAdapter = ScreenplayPoster.Adapter(
        screenplayIdAdapter = TmdbScreenplayIdAdapter
    )
    val ScreenplayVideoAdapter = ScreenplayVideo.Adapter(
        screenplayIdAdapter = TmdbScreenplayIdAdapter,
        idAdapter = TmdbVideoIdAdapter,
        resolutionAdapter = TmdbVideoResolutionAdapter,
        siteAdapter = TmdbVideoSiteAdapter,
        typeAdapter = TmdbVideoTypeAdapter
    )
    val SeasonAdapter = Season.Adapter(
        firstAirDateAdapter = DateAdapter,
        numberAdapter = IntLongAdapter,
        tmdbIdAdapter = TmdbSeasonIdAdapter,
        tmdbTvShowIdAdapter = TmdbTvShowIdAdapter,
        traktIdAdapter = TraktSeasonIdAdapter,
        traktTvShowIdAdapter = TraktTvShowIdAdapter
    )
    val SimilarAdapter = Similar.Adapter(
        similarTmdbIdAdapter = TmdbScreenplayIdAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )
    val SuggestionAdapter = Suggestion.Adapter(
        affinityAdapter = DoubleAdapter,
        sourceAdapter = SuggestionSourceAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
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
    val TrendingAdapter = Trending.Adapter(
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )
    val TvShowAdapter = TvShow.Adapter(
        firstAirDateAdapter = DateAdapter,
        runtimeAdapter = DurationAdapter,
        statusAdapter = TvShowStatusAdapter,
        tmdbIdAdapter = TmdbTvShowIdAdapter,
        traktIdAdapter = TraktTvShowIdAdapter
    )
    val VotingAdapter = Voting.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)
    val WatchlistAdapter = Watchlist.Adapter(
        tmdbIdAdapter = TmdbScreenplayIdAdapter,
        traktIdAdapter = TraktScreenplayIdAdapter
    )
}
