package cinescout.suggestions.presentation.model

import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.ScreenplayIds
import kotlinx.collections.immutable.ImmutableList

data class ForYouScreenplayUiModel(
    val screenplayIds: ScreenplayIds,
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
