package cinescout.database

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.SqlDriver
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [DatabaseAdapterModule::class, DatabaseQueriesModule::class])
class DatabaseModule {

    @Single
    fun driver() = Driver

    @Factory
    fun transacter(database: Database): Transacter = database

    @Single
    fun database(
        anticipatedAdapter: Anticipated.Adapter,
        appSettingsAdapter: AppSettings.Adapter,
        driver: SqlDriver,
        fetchDataAdapter: FetchData.Adapter,
        genreAdapter: Genre.Adapter,
        historyAdapter: History.Adapter,
        keywordAdapter: Keyword.Adapter,
        movieAdapter: Movie.Adapter,
        personAdapter: Person.Adapter,
        personalRatingAdapter: PersonalRating.Adapter,
        popularAdapter: Popular.Adapter,
        recommendationAdapter: Recommendation.Adapter,
        recommendedAdapter: Recommended.Adapter,
        screenplayBackdropAdapter: ScreenplayBackdrop.Adapter,
        screenplayCastMemberAdapter: ScreenplayCastMember.Adapter,
        screenplayCrewMemberAdapter: ScreenplayCrewMember.Adapter,
        screenplayGenreAdapter: ScreenplayGenre.Adapter,
        screenplayKeywordAdapter: ScreenplayKeyword.Adapter,
        screenplayPosterAdapter: ScreenplayPoster.Adapter,
        screenplayVideoAdapter: ScreenplayVideo.Adapter,
        similarAdapter: Similar.Adapter,
        suggestionAdapter: Suggestion.Adapter,
        traktAuthStateAdapter: TraktAuthState.Adapter,
        traktAccountAdapter: TraktAccount.Adapter,
        trendingAdapter: Trending.Adapter,
        tvShowAdapter: TvShow.Adapter,
        votingAdapter: Voting.Adapter,
        watchlistAdapter: Watchlist.Adapter
    ) = Database(
        anticipatedAdapter = anticipatedAdapter,
        appSettingsAdapter = appSettingsAdapter,
        driver = driver,
        fetchDataAdapter = fetchDataAdapter,
        genreAdapter = genreAdapter,
        historyAdapter = historyAdapter,
        keywordAdapter = keywordAdapter,
        movieAdapter = movieAdapter,
        personAdapter = personAdapter,
        personalRatingAdapter = personalRatingAdapter,
        popularAdapter = popularAdapter,
        recommendationAdapter = recommendationAdapter,
        recommendedAdapter = recommendedAdapter,
        screenplayBackdropAdapter = screenplayBackdropAdapter,
        screenplayCastMemberAdapter = screenplayCastMemberAdapter,
        screenplayCrewMemberAdapter = screenplayCrewMemberAdapter,
        screenplayGenreAdapter = screenplayGenreAdapter,
        screenplayKeywordAdapter = screenplayKeywordAdapter,
        screenplayPosterAdapter = screenplayPosterAdapter,
        screenplayVideoAdapter = screenplayVideoAdapter,
        similarAdapter = similarAdapter,
        suggestionAdapter = suggestionAdapter,
        traktAuthStateAdapter = traktAuthStateAdapter,
        traktAccountAdapter = traktAccountAdapter,
        trendingAdapter = trendingAdapter,
        tvShowAdapter = tvShowAdapter,
        votingAdapter = votingAdapter,
        watchlistAdapter = watchlistAdapter
    )
}

internal expect val Driver: SqlDriver
