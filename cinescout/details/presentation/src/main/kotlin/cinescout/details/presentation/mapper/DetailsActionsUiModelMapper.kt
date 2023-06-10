package cinescout.details.presentation.mapper

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import cinescout.details.domain.model.WithHistory
import cinescout.details.domain.model.WithPersonalRating
import cinescout.details.domain.model.WithScreenplay
import cinescout.details.domain.model.WithWatchlist
import cinescout.details.presentation.model.DetailsActionItemUiModel
import cinescout.details.presentation.model.DetailsActionsUiModel
import cinescout.progress.domain.model.MovieProgress
import cinescout.progress.domain.model.ScreenplayProgress
import cinescout.progress.domain.model.TvShowProgress
import cinescout.resources.ImageRes
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.Resource
import cinescout.resources.TextRes
import cinescout.screenplay.domain.model.Rating
import org.koin.core.annotation.Factory

@Factory
internal class DetailsActionsUiModelMapper {

    fun <T> toUiModel(
        item: T,
        progress: ScreenplayProgress
    ): DetailsActionsUiModel where T : WithScreenplay,
          T : WithHistory,
          T : WithPersonalRating,
          T : WithWatchlist {
        return DetailsActionsUiModel(
            historyUiModel = DetailsActionItemUiModel(
                badgeResource = progress.badgeResource(),
                contentDescription = TextRes(string.details_add_to_history),
                imageRes = ImageRes(drawable.ic_clock)
            ),
            personalRatingUiModel = DetailsActionItemUiModel(
                badgeResource = item.personalRating.badgeResource(),
                contentDescription = TextRes(string.details_rate_item),
                imageRes = item.personalRating.imageRes()
            ),
            watchlistUiModel = DetailsActionItemUiModel(
                badgeResource = none(),
                contentDescription = item.isInWatchlist.contentDescription(),
                imageRes = item.isInWatchlist.imageRes()
            )
        )
    }

    fun buildEmpty() = DetailsActionsUiModel(
        historyUiModel = DetailsActionItemUiModel(
            badgeResource = none(),
            contentDescription = TextRes(string.details_add_to_history),
            imageRes = ImageRes(drawable.ic_clock)
        ),
        personalRatingUiModel = DetailsActionItemUiModel(
            badgeResource = none(),
            contentDescription = TextRes(string.details_rate_item),
            imageRes = ImageRes(drawable.ic_star)
        ),
        watchlistUiModel = DetailsActionItemUiModel(
            badgeResource = none(),
            contentDescription = TextRes(string.details_add_to_watchlist),
            imageRes = ImageRes(drawable.ic_bookmark)
        )
    )

    private fun ScreenplayProgress.badgeResource(): Option<Resource> = when (this) {
        is MovieProgress.Unwatched, is TvShowProgress.Unwatched -> none()
        is MovieProgress.Watched, is TvShowProgress.Completed -> ImageRes(drawable.ic_check_round_color).some()
        is TvShowProgress.InProgress -> TextRes("${progress.value}%").some()
        is TvShowProgress.WaitingForNextEpisode -> ImageRes(drawable.ic_clock_color).some()
    }

    private fun Option<Rating>.badgeResource(): Option<TextRes> = fold(
        ifEmpty = { none() },
        ifSome = { TextRes(it.intValue.toString()).some() }
    )

    private fun Option<Rating>.imageRes(): ImageRes = fold(
        ifEmpty = { ImageRes(drawable.ic_star) },
        ifSome = { ImageRes(drawable.ic_star_filled) }
    )

    private fun Boolean.contentDescription(): TextRes = when (this) {
        true -> TextRes(string.details_remove_from_watchlist)
        false -> TextRes(string.details_add_to_watchlist)
    }

    private fun Boolean.imageRes(): ImageRes = when (this) {
        true -> ImageRes(drawable.ic_bookmark_filled)
        false -> ImageRes(drawable.ic_bookmark)
    }
}
