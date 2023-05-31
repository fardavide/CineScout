package cinescout.details.presentation.model

import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import kotlinx.collections.immutable.ImmutableList

data class ScreenplayDetailsUiModel(
    val creditsMember: ImmutableList<CreditsMember>,
    val genres: ImmutableList<String>,
    val backdrops: ImmutableList<String?>,
    val ids: ScreenplayIds,
    val isInWatchlist: Boolean,
    val overview: String,
    val posterUrl: String?,
    val ratings: ScreenplayRatingsUiModel,
    val releaseDate: String,
    val runtime: TextRes?,
    val title: String,
    val videos: ImmutableList<Video>
) {

    data class CreditsMember(
        val name: String,
        val profileImageUrl: String?,
        val role: String?
    )

    data class Video(
        val previewUrl: String,
        val title: String,
        val url: String
    )
}
