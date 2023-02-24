package cinescout.test.mock.builder

import cinescout.common.model.Rating
import cinescout.movies.domain.model.Movie
import cinescout.network.model.ConnectionStatus
import cinescout.network.testutil.addHandler
import cinescout.test.mock.TestSqlDriverModule
import cinescout.test.mock.model.MockAppConfig
import cinescout.tvshows.domain.model.TvShow
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module
import store.test.MockStoreOwner

@MockAppBuilderDsl
class MockAppConfigBuilder(
    private val tmdbMockEngine: MockEngine,
    private val traktMockEngine: MockEngine
) : KoinComponent {

    private var connectionStatus = ConnectionStatus.AllOnline
    private var forYouMovies: List<Movie> = emptyList()
    private var forYouTvShows: List<TvShow> = emptyList()
    private val modules: MutableList<Module> = mutableListOf()
    private var dislikedMovies: List<Movie> = emptyList()
    private var dislikedTvShows: List<TvShow> = emptyList()
    private var likedMovies: List<Movie> = emptyList()
    private var likedTvShows: List<TvShow> = emptyList()
    private var ratedMovies: Map<Movie, Rating> = emptyMap()
    private var ratedTvShows: Map<TvShow, Rating> = emptyMap()
    private var watchlistMovies: List<Movie> = emptyList()
    private var watchlistTvShows: List<TvShow> = emptyList()

    fun appScope(scope: CoroutineScope) {
        modules += module {
            single { scope }
        }
    }

    fun disliked(block: ListBuilder.() -> Unit) {
        dislikedMovies = ListBuilder().apply(block).movies
        dislikedTvShows = ListBuilder().apply(block).tvShows
    }

    fun forYou(block: ForYouBuilder.() -> Unit) {
        val builder = ForYouBuilder().apply(block)
        forYouMovies = builder.movies
        forYouTvShows = builder.tvShows
    }

    fun liked(block: ListBuilder.() -> Unit) {
        val builder = ListBuilder().apply(block)
        likedMovies = builder.movies
        likedTvShows = builder.tvShows
    }

    fun newInstall() {
        modules += TestSqlDriverModule
    }

    fun offline() {
        connectionStatus = connectionStatus.copy(
            device = ConnectionStatus.Connection.Offline
        )
    }

    fun rated(block: RatedListBuilder.() -> Unit) {
        val builder = RatedListBuilder().apply(block)
        ratedMovies = builder.movies
        ratedTvShows = builder.tvShows
    }

    fun tmdbNotReachable() {
        connectionStatus = connectionStatus.copy(
            tmdb = ConnectionStatus.Connection.Offline
        )
        tmdbMockEngine.addHandler { respondError(HttpStatusCode.ServiceUnavailable) }
    }

    fun traktNotReachable() {
        connectionStatus = connectionStatus.copy(
            trakt = ConnectionStatus.Connection.Offline
        )
        traktMockEngine.addHandler { respondError(HttpStatusCode.ServiceUnavailable) }
    }

    fun updatedCache() {
        modules += module {
            single { MockStoreOwner().updated() }
        }
    }

    fun watchlist(block: ListBuilder.() -> Unit) {
        val builder = ListBuilder().apply(block)
        watchlistMovies = builder.movies
        watchlistTvShows = builder.tvShows
    }

    fun build() = MockAppConfig(
        connectionStatus = connectionStatus,
        dislikedMovies = dislikedMovies,
        dislikedTvShows = dislikedTvShows,
        forYouMovies = forYouMovies,
        likedMovies = likedMovies,
        forYouTvShows = forYouTvShows,
        likedTvShows = likedTvShows,
        modules = modules,
        ratedMovies = ratedMovies,
        ratedTvShows = ratedTvShows,
        watchlistMovies = watchlistMovies,
        watchlistTvShows = watchlistTvShows
    )
}

