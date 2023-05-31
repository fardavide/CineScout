package cinescout.suggestions.presentation.model

import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import kotlinx.collections.immutable.ImmutableList

data class ForYouScreenplayUiModel(
    val screenplayIds: ScreenplayIds,
    val actors: ImmutableList<Actor>,
    val affinity: Int,
    val genres: ImmutableList<String>,
    val rating: String,
    val releaseDate: String,
    val suggestionSource: TextRes,
    val title: String
) {

    data class Actor(
        val profileImageUrl: String?
    )
}
