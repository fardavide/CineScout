package cinescout.test.mock.model

import cinescout.common.model.Rating
import cinescout.movies.domain.model.Movie
import cinescout.network.model.ConnectionStatus
import cinescout.tvshows.domain.model.TvShow
import org.koin.core.module.Module

data class MockAppConfig(
    val connectionStatus: ConnectionStatus,
    val dislikedMovies: List<Movie>,
    val dislikedTvShows: List<TvShow>,
    val forYouMovies: List<Movie>,
    val forYouTvShows: List<TvShow>,
    val likedMovies: List<Movie>,
    val likedTvShows: List<TvShow>,
    val modules: List<Module>,
    val ratedMovies: Map<Movie, Rating>,
    val ratedTvShows: Map<TvShow, Rating>,
    val watchlistMovies: List<Movie>,
    val watchlistTvShows: List<TvShow>
)
