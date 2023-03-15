package cinescout.test.mock

import cinescout.database.Database
import cinescout.di.kotlin.CineScoutModule
import cinescout.test.mock.model.MockAppConfig
import co.touchlab.kermit.Logger
import io.ktor.client.engine.mock.MockEngine
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest

class MockAppConfigApplier(
    private val config: MockAppConfig,
    private val tmdbMockEngine: MockEngine,
    private val traktMockEngine: MockEngine
) : KoinTest {

    private val mockEngineModule = module {
        single(named(MockEngineQualifier.Tmdb)) { tmdbMockEngine }
        single(named(MockEngineQualifier.Trakt)) { traktMockEngine }
    }

    fun apply(config: MockAppConfig = this.config) {
        getKoin().apply {
            Database.Schema.create(driver = get())
            loadModules(modules = config.modules)
            ConnectionManager.setConnection(config.connectionStatus)
            with(CacheManager) {
                addDislikes(config.dislikes)
                addLikes(config.likes)
                addRatings(config.ratings)
                addSuggestions(config.suggestions)
                addWatchlist(config.watchlist)
            }
        }
        logConfig(config)
    }

    fun setup() {
        val modules = CineScoutModule + config.modules + MockClientModule + mockEngineModule
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
        getKoin().unloadModules(CineScoutModule + config.modules + MockClientModule + mockEngineModule)
    }
}

@Suppress("ComplexMethod")
private fun logConfig(delegate: MockAppConfig) {
    Logger.withTag("MockAppRule").v {
        buildString {
            appendLine("Set Connection Status: ${delegate.connectionStatus}")
            if (delegate.dislikes.isNotEmpty()) {
                append("Added ${delegate.dislikes.size} movies to disliked. ")
            }
            if (delegate.likes.isNotEmpty()) {
                append("Added ${delegate.likes.size} movies to liked. ")
            }
            if (delegate.modules.isNotEmpty()) {
                append("Applied ${delegate.modules.size} modules. ")
            }
            if (delegate.ratings.isNotEmpty()) {
                append("Added ${delegate.ratings.size} movies to rated. ")
            }
            if (delegate.suggestions.isNotEmpty()) {
                append("Added ${delegate.suggestions.size} tv shows to for you. ")
            }
            if (delegate.watchlist.isNotEmpty()) {
                append("Added ${delegate.watchlist.size} movies to watchlist. ")
            }
        }
    }
}
