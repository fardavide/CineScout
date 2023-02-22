package cinescout.test.mock

import cinescout.di.kotlin.CineScoutModule
import cinescout.test.mock.builder.MockAppBuilderDsl
import cinescout.test.mock.builder.MockAppConfigBuilder
import cinescout.test.mock.model.MockAppConfig
import co.touchlab.kermit.Logger
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.core.context.startKoin
import org.koin.test.KoinTest

@MockAppBuilderDsl
fun MockAppRule(block: MockAppConfigBuilder.() -> Unit = {}): MockAppRule =
    MockAppRule(config = MockAppConfigBuilder().apply(block).build())

class MockAppRule internal constructor(
    private val config: MockAppConfig
) : TestWatcher(), KoinTest {

    operator fun invoke(block: MockAppConfigBuilder.() -> Unit) {
        apply(config = MockAppConfigBuilder().apply(block).build())
    }

    private fun apply(config: MockAppConfig = this.config) {
        getKoin().apply {
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

    override fun starting(description: Description) {
        try {
            getKoin().loadModules(CineScoutModule + config.modules + MockClientModule)
        } catch (e: IllegalStateException) {
            startKoin {
                modules(CineScoutModule + config.modules + MockClientModule)
            }
        }
        apply()
    }

    override fun finished(description: Description) {
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
