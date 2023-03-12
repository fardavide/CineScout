package cinescout.test.mock.model

import cinescout.network.model.ConnectionStatus
import cinescout.screenplay.domain.model.Movie
import cinescout.screenplay.domain.model.Rating
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.tvshows.domain.model.TvShow
import org.koin.core.module.Module

data class MockAppConfig(
    val connectionStatus: ConnectionStatus,
    val dislikedMovies: List<Movie>,
    val dislikedTvShows: List<TvShow>,
    val forYouMovies: List<SuggestedMovie>,
    val forYouTvShows: List<SuggestedTvShow>,
    val likedMovies: List<Movie>,
    val likedTvShows: List<TvShow>,
    val modules: List<Module>,
    val ratedMovies: Map<Movie, Rating>,
    val ratedTvShows: Map<TvShow, Rating>,
    val watchlistMovies: List<Movie>,
    val watchlistTvShows: List<TvShow>
)
