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

    private var forYouMovies: List<Movie> = emptyList()
    private var forYouTvShows: List<TvShow> = emptyList()
    private val modules: MutableList<Module> = mutableListOf()
    private var shouldDisableForYouHint = true
    private var dislikedMovies: List<Movie> = emptyList()
    private var dislikedTvShows: List<TvShow> = emptyList()
    private var likedMovies: List<Movie> = emptyList()
    private var likedTvShows: List<TvShow> = emptyList()
    private var ratedMovies: Map<Movie, Rating> = emptyMap()
    private var ratedTvShows: Map<TvShow, Rating> = emptyMap()
    private var watchlistMovies: List<Movie> = emptyList()
    private var watchlistTvShows: List<TvShow> = emptyList()

    fun disliked(block: ListBuilder.() -> Unit) {
        dislikedMovies = ListBuilder().apply(block).movies
        dislikedTvShows = ListBuilder().apply(block).tvShows
    }

    fun enableForYouHint() {
        shouldDisableForYouHint = false
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

    fun rated(block: RatedListBuilder.() -> Unit) {
        val builder = RatedListBuilder().apply(block)
        ratedMovies = builder.movies
        ratedTvShows = builder.tvShows
    }

    fun updatedCache() {
        modules += module {
            single<StoreOwner> { MockStoreOwner().updated() }
        }
    }

    fun watchlist(block: ListBuilder.() -> Unit) {
        val builder = ListBuilder().apply(block)
        watchlistMovies = builder.movies
        watchlistTvShows = builder.tvShows
    }

    internal fun build() = MockAppRuleDelegate(
        dislikedMovies = dislikedMovies,
        dislikedTvShows = dislikedTvShows,
        forYouMovies = forYouMovies,
        likedMovies = likedMovies,
        forYouTvShows = forYouTvShows,
        likedTvShows = likedTvShows,
        modules = modules,
        shouldDisableForYouHint = shouldDisableForYouHint,
        ratedMovies = ratedMovies,
        ratedTvShows = ratedTvShows,
        watchlistMovies = watchlistMovies,
        watchlistTvShows = watchlistTvShows
    )
}

internal data class MockAppRuleDelegate(
    val dislikedMovies: List<Movie>,
    val dislikedTvShows: List<TvShow>,
    val forYouMovies: List<Movie>,
    val forYouTvShows: List<TvShow>,
    val likedMovies: List<Movie>,
    val likedTvShows: List<TvShow>,
    val modules: List<Module>,
    val shouldDisableForYouHint: Boolean,
    val ratedMovies: Map<Movie, Rating>,
    val ratedTvShows: Map<TvShow, Rating>,
    val watchlistMovies: List<Movie>,
    val watchlistTvShows: List<TvShow>
)

internal expect val TestSqlDriverModule: Module
