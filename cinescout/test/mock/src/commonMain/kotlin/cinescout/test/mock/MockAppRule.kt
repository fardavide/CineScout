package cinescout.test.mock

import co.touchlab.kermit.Logger
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.test.KoinTest

@MockAppBuilderDsl
fun MockAppRule(block: MockAppRuleBuilder.() -> Unit = {}): MockAppRule =
    MockAppRule(delegate = MockAppRuleBuilder().apply(block).build())

class MockAppRule internal constructor(
    private val delegate: MockAppRuleDelegate
) : TestWatcher(), KoinTest {

    operator fun invoke(block: MockAppRuleBuilder.() -> Unit) {
        apply(delegate = MockAppRuleBuilder().apply(block).build())
    }

    private fun apply(delegate: MockAppRuleDelegate = this.delegate) {
        getKoin().apply {
            loadModules(modules = delegate.modules + MockClientModule)
            with(CacheManager) {
                addDislikedMovies(delegate.dislikedMovies)
                addDislikedTvShows(delegate.dislikedTvShows)
                addLikedMovies(delegate.likedMovies)
                addLikedTvShows(delegate.likedTvShows)
                addRatedMovies(delegate.ratedMovies)
                addRatedTvShows(delegate.ratedTvShows)
                addSuggestedMovies(delegate.forYouMovies)
                addSuggestedTvShows(delegate.forYouTvShows)
                addWatchlistMovies(delegate.watchlistMovies)
                addWatchlistTvShows(delegate.watchlistTvShows)
                if (delegate.shouldDisableForYouHint) {
                    disableForYouHint()
                }
            }
        }
        logConfig(delegate)
    }

    override fun starting(description: Description) {
        apply()
    }

    override fun finished(description: Description) {
        getKoin().unloadModules(delegate.modules + MockClientModule)
    }
}

@Suppress("ComplexMethod")
private fun logConfig(delegate: MockAppRuleDelegate) {
    Logger.withTag("MockAppRule").v {
        buildString {
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
}
