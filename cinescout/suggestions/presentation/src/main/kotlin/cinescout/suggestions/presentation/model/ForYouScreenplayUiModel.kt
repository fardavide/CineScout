package cinescout.suggestions.presentation.model

import cinescout.design.TextRes
import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.collections.immutable.ImmutableList

data class ForYouScreenplayUiModel(
    val tmdbScreenplayId: TmdbScreenplayId,
    val actors: ImmutableList<Actor>,
    val affinity: Int,
    val genres: ImmutableList<String>,
    val rating: String,
    val releaseYear: String,
    val suggestionSource: TextRes,
    val title: String
) {

    data class Actor(
        val profileImageUrl: String?
    )
}
