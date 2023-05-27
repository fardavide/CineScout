package cinescout.suggestions.presentation.mapper

import arrow.core.getOrElse
import cinescout.details.domain.model.WithCredits
import cinescout.details.domain.model.WithGenres
import cinescout.media.domain.model.TmdbProfileImage
import cinescout.people.domain.model.CastMember
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.suggestions.domain.model.SuggestionSource
import cinescout.suggestions.domain.model.WithSuggestedScreenplay
import cinescout.suggestions.presentation.model.ForYouScreenplayUiModel
import cinescout.utils.kotlin.format
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.Factory

internal interface ForYouItemUiModelMapper {

    fun <T> toUiModel(
        item: T
    ): ForYouScreenplayUiModel where T : WithSuggestedScreenplay,
          T : WithCredits,
          T : WithGenres
}

@Factory
internal class RealForYouItemUiModelMapper : ForYouItemUiModelMapper {

    override fun <T> toUiModel(
        item: T
    ): ForYouScreenplayUiModel where T : WithSuggestedScreenplay, T : WithCredits, T : WithGenres {
        return ForYouScreenplayUiModel(
            actors = toActorUiModels(item.credits.cast).toImmutableList(),
            affinity = item.affinity.value,
            genres = item.genres.genres.map { genre -> genre.name }.toImmutableList(),
            rating = item.screenplay.rating.average.value.format(digits = 1),
            releaseDate = item.screenplay.relevantDate.map { "${it.month.localName} ${it.year}" }.getOrElse { "" },
            suggestionSource = toSourceTextRes(item.source),
            title = item.screenplay.title,
            screenplayIds = item.screenplay.ids
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
        SuggestionSource.Recommended -> TextRes(string.suggestions_source_suggested)
        SuggestionSource.Trending -> TextRes(string.suggestions_source_trending)
        SuggestionSource.Anticipated -> TextRes(string.suggestions_source_upcoming)
    }
}
