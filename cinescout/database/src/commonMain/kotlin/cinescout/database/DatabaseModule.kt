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
        likedMovieAdapter: LikedMovie.Adapter,
        likedTvShowAdapter: LikedTvShow.Adapter,
        movieAdapter: Movie.Adapter,
        movieBackdropAdapter: MovieBackdrop.Adapter,
        movieCastMemberAdapter: MovieCastMember.Adapter,
        movieCrewMemberAdapter: MovieCrewMember.Adapter,
        movieGenreAdapter: MovieGenre.Adapter,
        movieKeywordAdapter: MovieKeyword.Adapter,
        moviePosterAdapter: MoviePoster.Adapter,
        movieRatingAdapter: MovieRating.Adapter,
        movieRecommendationAdapter: MovieRecommendation.Adapter,
        movieVideoAdapter: MovieVideo.Adapter,
        personAdapter: Person.Adapter,
        storeFetchDataAdapter: StoreFetchData.Adapter,
        suggestedMovieAdapter: SuggestedMovie.Adapter,
        suggestedTvShowAdapter: SuggestedTvShow.Adapter,
        traktAuthStateAdapter: TraktAuthState.Adapter,
        traktAccountAdapter: TraktAccount.Adapter,
        tvShowAdapter: TvShow.Adapter,
        tvShowBackdropAdapter: TvShowBackdrop.Adapter,
        tvShowCastMemberAdapter: TvShowCastMember.Adapter,
        tvShowCrewMemberAdapter: TvShowCrewMember.Adapter,
        tvShowGenreAdapter: TvShowGenre.Adapter,
        tvShowKeywordAdapter: TvShowKeyword.Adapter,
        tvShowPosterAdapter: TvShowPoster.Adapter,
        tvShowRatingAdapter: TvShowRating.Adapter,
        tvShowRecommendationAdapter: TvShowRecommendation.Adapter,
        tvShowVideoAdapter: TvShowVideo.Adapter,
        tvShowWatchlistAdapter: TvShowWatchlist.Adapter,
        watchlistAdapter: Watchlist.Adapter
    ) = Database(
        driver = driver,
        genreAdapter = genreAdapter,
        keywordAdapter = keywordAdapter,
        likedMovieAdapter = likedMovieAdapter,
        likedTvShowAdapter = likedTvShowAdapter,
        movieAdapter = movieAdapter,
        movieBackdropAdapter = movieBackdropAdapter,
        movieCastMemberAdapter = movieCastMemberAdapter,
        movieCrewMemberAdapter = movieCrewMemberAdapter,
        movieGenreAdapter = movieGenreAdapter,
        movieKeywordAdapter = movieKeywordAdapter,
        moviePosterAdapter = moviePosterAdapter,
        movieRatingAdapter = movieRatingAdapter,
        movieRecommendationAdapter = movieRecommendationAdapter,
        movieVideoAdapter = movieVideoAdapter,
        personAdapter = personAdapter,
        storeFetchDataAdapter = storeFetchDataAdapter,
        suggestedMovieAdapter = suggestedMovieAdapter,
        suggestedTvShowAdapter = suggestedTvShowAdapter,
        traktAuthStateAdapter = traktAuthStateAdapter,
        traktAccountAdapter = traktAccountAdapter,
        tvShowAdapter = tvShowAdapter,
        tvShowBackdropAdapter = tvShowBackdropAdapter,
        tvShowCastMemberAdapter = tvShowCastMemberAdapter,
        tvShowCrewMemberAdapter = tvShowCrewMemberAdapter,
        tvShowGenreAdapter = tvShowGenreAdapter,
        tvShowKeywordAdapter = tvShowKeywordAdapter,
        tvShowPosterAdapter = tvShowPosterAdapter,
        tvShowRatingAdapter = tvShowRatingAdapter,
        tvShowRecommendationAdapter = tvShowRecommendationAdapter,
        tvShowVideoAdapter = tvShowVideoAdapter,
        tvShowWatchlistAdapter = tvShowWatchlistAdapter,
        watchlistAdapter = watchlistAdapter
    )
}

internal expect val Driver: SqlDriver
