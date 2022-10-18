package cinescout.test.mock

import cinescout.common.model.Rating
import cinescout.movies.domain.model.Movie
import cinescout.tvshows.domain.model.TvShow
import org.koin.core.module.Module
import org.koin.dsl.module
import store.StoreOwner
import store.test.MockStoreOwner

@MockAppBuilderDsl
class MockAppRuleBuilder internal constructor() {

    private var forYouItems: List<Movie> = emptyList()
    private val modules: MutableList<Module> = mutableListOf()
    private var shouldDisableForYouHint = true
    private var ratedMovies: Map<Movie, Rating> = emptyMap()
    private var ratedTvShows: Map<TvShow, Rating> = emptyMap()
    private var watchlistMovies: List<Movie> = emptyList()
    private var watchlistTvShows: List<TvShow> = emptyList()

    fun enableForYouHint() {
        shouldDisableForYouHint = false
    }

    fun forYou(block: ForYouBuilder.() -> Unit) {
        forYouItems = ForYouBuilder().apply(block).items
    }

    fun newInstall() {
        modules += TestSqlDriverModule
    }

    fun rated(block: RatedListBuilder.() -> Unit) {
        ratedMovies = RatedListBuilder().apply(block).movies
        ratedTvShows = RatedListBuilder().apply(block).tvShows
    }

    fun updatedCache() {
        modules += module {
            single<StoreOwner> { MockStoreOwner().updated() }
        }
    }

    fun watchlist(block: ListBuilder.() -> Unit) {
        watchlistMovies = ListBuilder().apply(block).movies
        watchlistTvShows = ListBuilder().apply(block).tvShows
    }

    internal fun build() = MockAppRuleDelegate(
        forYouItems = forYouItems,
        modules = modules,
        shouldDisableForYouHint = shouldDisableForYouHint,
        ratedMovies = ratedMovies,
        ratedTvShows = ratedTvShows,
        watchlistMovies = watchlistMovies,
        watchlistTvShows = watchlistTvShows
    )
}

internal data class MockAppRuleDelegate(
    val forYouItems: List<Movie>,
    val modules: List<Module>,
    val shouldDisableForYouHint: Boolean,
    val ratedMovies: Map<Movie, Rating>,
    val ratedTvShows: Map<TvShow, Rating>,
    val watchlistMovies: List<Movie>,
    val watchlistTvShows: List<TvShow>
)

internal expect val TestSqlDriverModule: Module
