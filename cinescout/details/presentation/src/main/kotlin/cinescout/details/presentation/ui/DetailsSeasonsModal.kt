package cinescout.details.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.Modal
import cinescout.details.presentation.model.DetailsEpisodeUiModel
import cinescout.details.presentation.model.DetailsSeasonUiModel
import cinescout.details.presentation.model.DetailsSeasonsUiModel
import cinescout.details.presentation.sample.DetailsSeasonsUiModelSample
import cinescout.details.presentation.ui.component.DetailsNavigableRow
import cinescout.history.domain.usecase.AddToHistory
import cinescout.resources.R.string
import cinescout.resources.TextRes
import cinescout.resources.string

@Composable
internal fun DetailsSeasonsModal(uiModel: DetailsSeasonsUiModel, actions: DetailsSeasonsModal.Actions) {
    Modal(onDismiss = actions.dismiss) {
        LazyColumn {
            items(uiModel.seasonUiModels) { seasonUiModel ->
                DetailsNavigableRow(
                    modifier = Modifier.padding(bottom = Dimens.Margin.Medium),
                    onClick = { actions.openEpisodes(seasonUiModel) },
                    contentDescription = TextRes(string.details_see_episodes)
                ) {
                    Row {
                        Column(
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(Dimens.Margin.Small)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.XSmall),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = seasonUiModel.title, style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.width(Dimens.Margin.XSmall))
                                Text(
                                    text = string(textRes = seasonUiModel.totalEpisodes),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Text(
                                    modifier = Modifier.alpha(0.32f),
                                    text = string(textRes = seasonUiModel.watchedEpisodes),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                progress = seasonUiModel.progress
                            )
                        }
                        RadioButton(
                            selected = seasonUiModel.completed,
                            onClick = {
                                val params = AddToHistory.Params.Season(
                                    tvShowIds = seasonUiModel.tvShowIds,
                                    seasonIds = seasonUiModel.seasonIds,
                                    episodes = seasonUiModel.episodeUiModels
                                        .map(DetailsEpisodeUiModel::seasonAndEpisodeNumber)
                                )
                                val modalParam = AddToHistoryModal.Params(
                                    itemTitle = seasonUiModel.title,
                                    addToHistoryParams = params
                                )
                                actions.addToHistory(modalParam)
                            }
                        )
                    }
                }
            }
        }
    }
}

object DetailsSeasonsModal {

    data class Actions(
        val addToHistory: (AddToHistoryModal.Params) -> Unit,
        val dismiss: () -> Unit,
        val openEpisodes: (DetailsSeasonUiModel) -> Unit
    ) {

        companion object {

            val Empty = Actions(
                addToHistory = {},
                dismiss = {},
                openEpisodes = {}
            )
        }
    }
}

@Preview
@Composable
private fun DetailsSeasonsModalPreview() {
    CineScoutTheme {
        DetailsSeasonsModal(
            uiModel = DetailsSeasonsUiModelSample.BreakingBad_OneSeasonAndTwoEpisodesWatched,
            actions = DetailsSeasonsModal.Actions.Empty
        )
    }
}
