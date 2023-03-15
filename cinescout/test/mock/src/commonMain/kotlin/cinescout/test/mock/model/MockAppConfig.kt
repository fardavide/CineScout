package cinescout.test.mock.model

import cinescout.network.model.ConnectionStatus
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.suggestions.domain.model.SuggestedScreenplay
import org.koin.core.module.Module

data class MockAppConfig(
    val connectionStatus: ConnectionStatus,
    val dislikes: List<Screenplay>,
    val likes: List<Screenplay>,
    val modules: List<Module>,
    val ratings: Map<Screenplay, Rating>,
    val suggestions: List<SuggestedScreenplay>,
    val watchlist: List<Screenplay>
)
