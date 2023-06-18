package cinescout.details.presentation.model

import androidx.compose.runtime.Composable
import cinescout.details.presentation.state.DetailsSeasonsState
import cinescout.resources.TextRes
import cinescout.resources.string
import cinescout.screenplay.domain.model.ids.ScreenplayIds
import kotlinx.collections.immutable.ImmutableList

data class ScreenplayDetailsUiModel(
    val creditsMembers: ImmutableList<CreditsMember>,
    val genres: ImmutableList<String>,
    val backdrops: ImmutableList<String?>,
    val ids: ScreenplayIds,
    val overview: String,
    val personalRating: Int?,
    val posterUrl: String?,
    val premiere: Premiere,
    val ratingAverage: String,
    val ratingCount: TextRes,
    val runtime: TextRes?,
    val seasonsState: DetailsSeasonsState,
    val tagline: String?,
    val title: String,
    val videos: ImmutableList<Video>
) {

    data class CreditsMember(
        val name: String,
        val profileImageUrl: String?,
        val role: String?
    )

    data class Premiere(
        val releaseDate: String?,
        val status: TextRes?
    ) {

        @Composable
        fun string(): String {
            val statusString = status?.let { string(textRes = it) }
            return listOfNotNull(releaseDate, statusString).joinToString(separator = " • ")
        }
    }

    data class Video(
        val previewUrl: String,
        val title: String,
        val url: String
    )
}
