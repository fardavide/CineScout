package cinescout.test.mock

import cinescout.database.Database
import cinescout.di.kotlin.CineScoutModule
import cinescout.test.mock.model.MockAppConfig
import co.touchlab.kermit.Logger
import org.koin.core.context.startKoin
import org.koin.test.KoinTest

class MockAppConfigApplier(private val config: MockAppConfig) : KoinTest {

    fun apply(config: MockAppConfig = this.config) {
        getKoin().apply {
            Database.Schema.create(driver = get())
            loadModules(modules = config.modules)
            ConnectionManager.setConnection(config.connectionStatus)
            with(CacheManager) {
                addDislikedMovies(config.dislikedMovies)
                addDislikedTvShows(config.dislikedTvShows)
                addLikedMovies(config.likedMovies)
                addLikedTvShows(config.likedTvShows)
                addRatedMovies(config.ratedMovies)
                addRatedTvShows(config.ratedTvShows)
                addSuggestedMovies(config.forYouMovies)
                addSuggestedTvShows(config.forYouTvShows)
                addWatchlistMovies(config.watchlistMovies)
                addWatchlistTvShows(config.watchlistTvShows)
            }
        }
        logConfig(config)
    }

    fun setup() {
        val modules = CineScoutModule + config.modules + MockClientModule
        try {
            getKoin().loadModules(modules)
        } catch (e: IllegalStateException) { // Koin is not started
            startKoin {
                allowOverride(true)
                modules(modules)
            }
        }
        apply()
    }

    fun teardown() {
        getKoin().unloadModules(CineScoutModule + config.modules + MockClientModule)
    }
}

@Suppress("ComplexMethod")
private fun logConfig(delegate: MockAppConfig) {
    Logger.withTag("MockAppRule").v {
        buildString {
            appendLine("Set Connection Status: ${delegate.connectionStatus}")
            if (delegate.dislikedMovies.isNotEmpty()) {
                append("Added ${delegate.dislikedMovies.size} movies to disliked. ")
            }
            if (delegate.dislikedTvShows.isNotEmpty()) {
                append("Added ${delegate.dislikedTvShows.size} tv shows to disliked. ")
            }
            if (delegate.modules.isNotEmpty()) {
                append("Applied ${delegate.modules.size} modules. ")
            }
            if (delegate.likedMovies.isNotEmpty()) {
                append("Added ${delegate.likedMovies.size} movies to liked. ")
            }
            if (delegate.likedTvShows.isNotEmpty()) {
                append("Added ${delegate.likedTvShows.size} tv shows to liked. ")
            }
            if (delegate.forYouMovies.isNotEmpty()) {
                append("Added ${delegate.forYouMovies.size} movies to for you. ")
            }
            if (delegate.forYouTvShows.isNotEmpty()) {
                append("Added ${delegate.forYouTvShows.size} tv shows to for you. ")
            }
            if (delegate.ratedMovies.isNotEmpty()) {
                append("Added ${delegate.ratedMovies.size} movies to rated. ")
            }
            if (delegate.ratedTvShows.isNotEmpty()) {
                append("Added ${delegate.ratedTvShows.size} tv shows to rated. ")
            }
            if (delegate.watchlistMovies.isNotEmpty()) {
                append("Added ${delegate.watchlistMovies.size} movies to watchlist. ")
            }
            if (delegate.watchlistTvShows.isNotEmpty()) {
                append("Added ${delegate.watchlistTvShows.size} tv shows to watchlist. ")
            }
        }
    }
}
