package cinescout.test.mock

import arrow.core.toNonEmptyListOrNull
import cinescout.rating.domain.usecase.RateScreenplay
import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.suggestions.domain.repository.SuggestionRepository
import cinescout.voting.domain.usecase.SetDisliked
import cinescout.voting.domain.usecase.SetLiked
import cinescout.watchlist.domain.usecase.AddToWatchlist
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal object CacheManager : KoinComponent {

    fun addCached(screenplays: List<Screenplay>) {
        runBlocking {
            insertScreenplays(screenplays)
        }
    }

    fun addDislikes(screenplays: List<Screenplay>) {
        runBlocking {
            insertScreenplays(screenplays)
            with(get<SetDisliked>()) {
                for (screenplay in screenplays) invoke(screenplay.tmdbId)
            }
        }
    }

    fun addLikes(screenplays: List<Screenplay>) {
        runBlocking {
            insertScreenplays(screenplays)
            with(get<SetLiked>()) {
                for (screenplay in screenplays) invoke(screenplay.tmdbId)
            }
        }
    }

    fun addSuggestions(screenplays: List<SuggestedScreenplay>) {
        val nonEmptyScreenplays = screenplays.toNonEmptyListOrNull()
            ?: return
        runBlocking {
            insertScreenplays(nonEmptyScreenplays.map { it.screenplay })
            get<SuggestionRepository>().storeSuggestions(nonEmptyScreenplays)
        }
    }

    fun addRatings(screenplays: Map<Screenplay, Rating>) {
        runBlocking {
            insertScreenplays(screenplays.keys.toList())
            with(get<RateScreenplay>()) {
                for ((screenplay, rating) in screenplays) invoke(screenplay.ids, rating)
            }
        }
    }

    fun addWatchlist(screenplays: List<Screenplay>) {
        runBlocking {
            insertScreenplays(screenplays)
            with(get<AddToWatchlist>()) {
                for (screenplay in screenplays) invoke(screenplay.ids)
            }
        }
    }

    private suspend fun insertScreenplays(screenplays: List<Screenplay>) {
        get<LocalScreenplayDataSource>().insert(screenplays)
    }
}
