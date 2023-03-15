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
        movieBackdropAdapter: MovieBackdrop.Adapter,
        movieGenreAdapter: MovieGenre.Adapter,
        movieKeywordAdapter: MovieKeyword.Adapter,
        moviePosterAdapter: MoviePoster.Adapter,
        movieVideoAdapter: MovieVideo.Adapter,
        personAdapter: Person.Adapter,
        personalRatingAdapter: PersonalRating.Adapter,
        recommendationAdapter: Recommendation.Adapter,
        screenplayCastMemberAdapter: ScreenplayCastMember.Adapter,
        screenplayCrewMemberAdapter: ScreenplayCrewMember.Adapter,
        similarAdapter: Similar.Adapter,
        storeFetchDataAdapter: StoreFetchData.Adapter,
        suggestionAdapter: Suggestion.Adapter,
        traktAuthStateAdapter: TraktAuthState.Adapter,
        traktAccountAdapter: TraktAccount.Adapter,
        tvShowAdapter: TvShow.Adapter,
        tvShowBackdropAdapter: TvShowBackdrop.Adapter,
        tvShowGenreAdapter: TvShowGenre.Adapter,
        tvShowKeywordAdapter: TvShowKeyword.Adapter,
        tvShowPosterAdapter: TvShowPoster.Adapter,
        tvShowVideoAdapter: TvShowVideo.Adapter,
        votingAdapter: Voting.Adapter,
        watchlistAdapter: Watchlist.Adapter
    ) = Database(
        driver = driver,
        genreAdapter = genreAdapter,
        keywordAdapter = keywordAdapter,
        movieAdapter = movieAdapter,
        movieBackdropAdapter = movieBackdropAdapter,
        movieGenreAdapter = movieGenreAdapter,
        movieKeywordAdapter = movieKeywordAdapter,
        moviePosterAdapter = moviePosterAdapter,
        movieVideoAdapter = movieVideoAdapter,
        personAdapter = personAdapter,
        personalRatingAdapter = personalRatingAdapter,
        recommendationAdapter = recommendationAdapter,
        screenplayCastMemberAdapter = screenplayCastMemberAdapter,
        screenplayCrewMemberAdapter = screenplayCrewMemberAdapter,
        similarAdapter = similarAdapter,
        storeFetchDataAdapter = storeFetchDataAdapter,
        suggestionAdapter = suggestionAdapter,
        traktAuthStateAdapter = traktAuthStateAdapter,
        traktAccountAdapter = traktAccountAdapter,
        tvShowAdapter = tvShowAdapter,
        tvShowBackdropAdapter = tvShowBackdropAdapter,
        tvShowGenreAdapter = tvShowGenreAdapter,
        tvShowKeywordAdapter = tvShowKeywordAdapter,
        tvShowPosterAdapter = tvShowPosterAdapter,
        tvShowVideoAdapter = tvShowVideoAdapter,
        votingAdapter = votingAdapter,
        watchlistAdapter = watchlistAdapter
    )
}

internal expect val Driver: SqlDriver
