package cinescout.test.mock

import co.touchlab.kermit.Logger
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.test.KoinTest

@MockAppBuilderDsl
fun MockAppRule(block: MockAppRuleBuilder.() -> Unit): MockAppRule =
    MockAppRule(delegate = MockAppRuleBuilder().apply(block).build())

class MockAppRule internal constructor(
    private val delegate: MockAppRuleDelegate
) : TestRule, KoinTest {

    override fun apply(base: Statement, description: Description): Statement {
        getKoin().apply {
            loadModules(modules = delegate.modules + MockClientModule)
            CacheManager.addSuggestedMovies(delegate.forYouItems)
            CacheManager.addRatedMovies(delegate.ratedMovies)
            CacheManager.addRatedTvShows(delegate.ratedTvShows)
            CacheManager.addWatchlistMovies(delegate.watchlistMovies)
            CacheManager.addWatchlistTvShows(delegate.watchlistTvShows)
            if (delegate.shouldDisableForYouHint) {
                CacheManager.disableForYouHint()
            }
        }
        Logger.withTag("MockAppRule").v {
            buildString {
                if (delegate.modules.isNotEmpty()) {
                    append("Applied ${delegate.modules.size} modules. ")
                }
                if (delegate.forYouItems.isNotEmpty()) {
                    append("Added ${delegate.forYouItems.size} movies to for you. ")
                }
                if (delegate.shouldDisableForYouHint) {
                    append("For you hint disabled. ")
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
        return base
    }
}
