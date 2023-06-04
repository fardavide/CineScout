package cinescout.details.presentation.model

import arrow.core.Option
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import kotlinx.collections.immutable.ImmutableList

data class ScreenplayDetailsUiModel(
    val creditsMember: ImmutableList<CreditsMember>,
    val genres: ImmutableList<String>,
    val backdrops: ImmutableList<String?>,
    val ids: ScreenplayIds,
    val overview: String,
    val personalRating: Option<Int>,
    val posterUrl: String?,
    val ratingAverage: String,
    val ratingCount: TextRes,
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
