package cinescout.test.mock

import arrow.core.toNonEmptyListOrNull
import cinescout.details.domain.model.ScreenplayWithExtras
import cinescout.details.domain.sample.ScreenplayWithExtrasSample
import cinescout.rating.domain.usecase.RateScreenplay
import cinescout.screenplay.data.datasource.LocalScreenplayDataSource
import cinescout.screenplay.domain.model.Rating
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.sample.ScreenplaySample
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedScreenplay
import cinescout.voting.domain.usecase.SetDisliked
import cinescout.voting.domain.usecase.SetLiked
import cinescout.watchlist.domain.usecase.AddToWatchlist
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal object CacheManager : KoinComponent {

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

@Suppress("unused")
private fun Screenplay.withExtras(): ScreenplayWithExtras = when (this) {
    ScreenplaySample.BreakingBad -> ScreenplayWithExtrasSample.BreakingBad
    ScreenplaySample.Dexter -> ScreenplayWithExtrasSample.Dexter
    ScreenplaySample.Grimm -> ScreenplayWithExtrasSample.Grimm
    ScreenplaySample.Inception -> ScreenplayWithExtrasSample.Inception
    ScreenplaySample.TheWolfOfWallStreet -> ScreenplayWithExtrasSample.TheWolfOfWallStreet
    ScreenplaySample.War -> ScreenplayWithExtrasSample.War
    else -> throw UnsupportedOperationException("Screenplay $this is not supported")
}
