package cinescout.suggestions.presentation.mapper

import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.domain.model.CastMember
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.suggestions.domain.model.SuggestedScreenplayWithExtras
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

internal interface ForYouItemUiModelMapper {

    fun toUiModel(suggestedScreenplayWithExtras: SuggestedScreenplayWithExtras): ForYouScreenplayUiModel
}

@Factory
internal class RealForYouItemUiModelMapper : ForYouItemUiModelMapper {

    override fun toUiModel(
        suggestedScreenplayWithExtras: SuggestedScreenplayWithExtras
    ): ForYouScreenplayUiModel {
        val screenplay = suggestedScreenplayWithExtras.screenplay
        return ForYouScreenplayUiModel(
            actors = toActorUiModels(suggestedScreenplayWithExtras.credits.cast).toImmutableList(),
            affinity = suggestedScreenplayWithExtras.affinity.value,
            genres = suggestedScreenplayWithExtras.genres.genres.map { genre -> genre.name }.toImmutableList(),
            rating = screenplay.rating.average.value.toString(),
            releaseYear = screenplay.relevantDate.orNull()?.year?.toString().orEmpty(),
            suggestionSource = toSourceTextRes(suggestedScreenplayWithExtras.source),
            title = screenplay.title,
            tmdbScreenplayId = screenplay.tmdbId
        )
    }

    private fun toActorUiModels(actors: List<CastMember>): List<ForYouScreenplayUiModel.Actor> =
        actors.map { member ->
            val profileImageUrl = member.person.profileImage
                .map { image -> image.getUrl(TmdbProfileImage.Size.SMALL) }
                .orNull()
            ForYouScreenplayUiModel.Actor(profileImageUrl = profileImageUrl)
        }

    private fun toSourceTextRes(source: SuggestionSource): TextRes = when (source) {
        is SuggestionSource.FromLiked -> TextRes(string.suggestions_source_liked, source.title)
        is SuggestionSource.FromRated -> TextRes(string.suggestions_source_liked, source.title)
        is SuggestionSource.FromWatchlist -> TextRes(string.suggestions_source_liked, source.title)
        is SuggestionSource.PersonalSuggestions -> TextRes(string.suggestions_source_personal_suggestion)
        SuggestionSource.Popular -> TextRes(string.suggestions_source_popular)
        SuggestionSource.Suggested -> TextRes(string.suggestions_source_suggested)
        SuggestionSource.Trending -> TextRes(string.suggestions_source_trending)
        SuggestionSource.Upcoming -> TextRes(string.suggestions_source_upcoming)
    }
}
