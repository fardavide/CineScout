package cinescout.database.testutil

import cinescout.database.Genre
import cinescout.database.Keyword
import cinescout.database.Movie
import cinescout.database.Person
import cinescout.database.PersonalRating
import cinescout.database.Recommendation
import cinescout.database.ScreenplayBackdrop
import cinescout.database.ScreenplayCastMember
import cinescout.database.ScreenplayCrewMember
import cinescout.database.ScreenplayGenre
import cinescout.database.ScreenplayKeyword
import cinescout.database.ScreenplayPoster
import cinescout.database.ScreenplayVideo
import cinescout.database.Similar
import cinescout.database.StoreFetchData
import cinescout.database.Suggestion
import cinescout.database.TraktAccount
import cinescout.database.TraktAuthState
import cinescout.database.TvShow
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
    val MovieAdapter = Movie.Adapter(
        releaseDateAdapter = DateAdapter,
        tmdbIdAdapter = TmdbMovieIdAdapter
    )
    val PersonAdapter = Person.Adapter(tmdbIdAdapter = TmdbPersonIdAdapter)
    val PersonalRatingAdapter = PersonalRating.Adapter(
        ratingAdapter = IntAdapter,
        tmdbIdAdapter = TmdbScreenplayIdAdapter
    )
    val RecommendationAdapter = Recommendation.Adapter(
        screenplayIdAdapter = TmdbScreenplayIdAdapter
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
    val VotingAdapter = Voting.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)
    val WatchlistAdapter = Watchlist.Adapter(tmdbIdAdapter = TmdbScreenplayIdAdapter)
}
