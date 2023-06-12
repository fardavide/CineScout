package cinescout.details.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.Modal
import cinescout.details.presentation.model.DetailsSeasonUiModel
import cinescout.details.presentation.sample.DetailsSeasonUiModelSample
import cinescout.history.domain.usecase.AddToHistory
import cinescout.resources.string

@Composable
internal fun DetailsEpisodesModal(
    uiModel: DetailsSeasonUiModel,
    addToHistory: (AddToHistoryModal.Params) -> Unit,
    dismiss: () -> Unit
) {
    Modal(onDismiss = dismiss) {
        LazyColumn {
            stickyHeader {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(bottom = Dimens.Margin.Small),
                    text = uiModel.title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
            }
            items(uiModel.episodeUiModels) { episodeUiModel ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.Margin.Medium)
                        .padding(bottom = Dimens.Margin.Small),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.Small)
                    ) {
                        Text(
                            text = string(textRes = episodeUiModel.episodeNumber),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = episodeUiModel.title, style = MaterialTheme.typography.labelMedium)
                    }
                    RadioButton(
                        selected = episodeUiModel.watched,
                        onClick = {
                            val params = AddToHistory.Params.Episode(
                                tvShowIds = uiModel.tvShowIds,
                                episodeIds = episodeUiModel.episodeIds,
                                episode = episodeUiModel.seasonAndEpisodeNumber
                            )
                            val modalParams = AddToHistoryModal.Params(
                                itemTitle = episodeUiModel.title,
                                addToHistoryParams = params
                            )
                            addToHistory(modalParams)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DetailsEpisodesModalPreview() {
    CineScoutTheme {
        DetailsEpisodesModal(
            uiModel = DetailsSeasonUiModelSample.BreakingBad_s2_OneEpisodeWatched,
            addToHistory = {},
            dismiss = {}
        )
    }
}
