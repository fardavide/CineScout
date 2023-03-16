package cinescout.test.mock.builder

import cinescout.network.model.ConnectionStatus
import cinescout.network.testutil.addHandler
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.test.mock.TestSqlDriverModule
import cinescout.test.mock.model.MockAppConfig
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

@MockAppBuilderDsl
class MockAppConfigBuilder(
    private val tmdbMockEngine: MockEngine,
    private val traktMockEngine: MockEngine
) : KoinComponent {

    private var connectionStatus = ConnectionStatus.AllOnline
    private var dislikes: List<Screenplay> = emptyList()
    private val modules: MutableList<Module> = mutableListOf()
    private var likes: List<Screenplay> = emptyList()
    private var ratings: Map<Screenplay, Rating> = emptyMap()
    private var suggestions: List<SuggestedScreenplay> = emptyList()
    private var watchlist: List<Screenplay> = emptyList()

    fun appScope(scope: CoroutineScope) {
        modules += module {
            single { scope }
        }
    }

    fun disliked(block: ListBuilder.() -> Unit) {
        dislikes = ListBuilder().apply(block).list
    }

    fun forYou(block: ForYouBuilder.() -> Unit) {
        suggestions = ForYouBuilder().apply(block).list
    }

    fun liked(block: ListBuilder.() -> Unit) {
        likes = ListBuilder().apply(block).list
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
        ratings = RatedListBuilder().apply(block).map
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

    @Deprecated("Alternative needed")
    fun updatedCache() {
        // TODO
    }

    fun watchlist(block: ListBuilder.() -> Unit) {
        watchlist = ListBuilder().apply(block).list
    }

    fun build() = MockAppConfig(
        connectionStatus = connectionStatus,
        dislikes = dislikes,
        likes = likes,
        modules = modules,
        ratings = ratings,
        watchlist = watchlist,
        suggestions = suggestions
    )
}

