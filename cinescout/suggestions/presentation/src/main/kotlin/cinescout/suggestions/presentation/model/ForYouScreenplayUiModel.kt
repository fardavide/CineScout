package cinescout.suggestions.presentation.model

import cinescout.screenplay.domain.model.TmdbScreenplayId
import kotlinx.collections.immutable.ImmutableList

data class ForYouScreenplayUiModel(
    val tmdbScreenplayId: TmdbScreenplayId,
    val actors: ImmutableList<Actor>,
    val backdropUrl: String?,
    val genres: ImmutableList<String>,
    val posterUrl: String?,
    val rating: String,
    val releaseYear: String,
    val title: String
) {

    data class Actor(
        val profileImageUrl: String?
    )
}
