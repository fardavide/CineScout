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
        driver: SqlDriver,
        genreAdapter: Genre.Adapter,
        keywordAdapter: Keyword.Adapter,
        movieAdapter: Movie.Adapter,
        personAdapter: Person.Adapter,
        personalRatingAdapter: PersonalRating.Adapter,
        recommendationAdapter: Recommendation.Adapter,
        screenplayBackdropAdapter: ScreenplayBackdrop.Adapter,
        screenplayCastMemberAdapter: ScreenplayCastMember.Adapter,
        screenplayCrewMemberAdapter: ScreenplayCrewMember.Adapter,
        screenplayGenreAdapter: ScreenplayGenre.Adapter,
        screenplayKeywordAdapter: ScreenplayKeyword.Adapter,
        screenplayPosterAdapter: ScreenplayPoster.Adapter,
        screenplayVideoAdapter: ScreenplayVideo.Adapter,
        similarAdapter: Similar.Adapter,
        storeFetchDataAdapter: StoreFetchData.Adapter,
        suggestionAdapter: Suggestion.Adapter,
        traktAuthStateAdapter: TraktAuthState.Adapter,
        traktAccountAdapter: TraktAccount.Adapter,
        tvShowAdapter: TvShow.Adapter,
        votingAdapter: Voting.Adapter,
        watchlistAdapter: Watchlist.Adapter
    ) = Database(
        driver = driver,
        genreAdapter = genreAdapter,
        keywordAdapter = keywordAdapter,
        movieAdapter = movieAdapter,
        personAdapter = personAdapter,
        personalRatingAdapter = personalRatingAdapter,
        recommendationAdapter = recommendationAdapter,
        screenplayBackdropAdapter = screenplayBackdropAdapter,
        screenplayCastMemberAdapter = screenplayCastMemberAdapter,
        screenplayCrewMemberAdapter = screenplayCrewMemberAdapter,
        screenplayGenreAdapter = screenplayGenreAdapter,
        screenplayKeywordAdapter = screenplayKeywordAdapter,
        screenplayPosterAdapter = screenplayPosterAdapter,
        screenplayVideoAdapter = screenplayVideoAdapter,
        similarAdapter = similarAdapter,
        storeFetchDataAdapter = storeFetchDataAdapter,
        suggestionAdapter = suggestionAdapter,
        traktAuthStateAdapter = traktAuthStateAdapter,
        traktAccountAdapter = traktAccountAdapter,
        tvShowAdapter = tvShowAdapter,
        votingAdapter = votingAdapter,
        watchlistAdapter = watchlistAdapter
    )
}

internal expect val Driver: SqlDriver
