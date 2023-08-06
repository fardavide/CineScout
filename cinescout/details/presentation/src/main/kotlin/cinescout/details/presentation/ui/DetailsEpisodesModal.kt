package cinescout.details.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
    Modal(onDismiss = dismiss) { DetailsEpisodesModalContent(uiModel, addToHistory) }
}

@Composable
private fun DetailsEpisodesModalContent(
    uiModel: DetailsSeasonUiModel,
    addToHistory: (AddToHistoryModal.Params) -> Unit
) {
    LazyColumn {
        stickyHeader {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.Margin.small),
                text = uiModel.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
        items(uiModel.episodeUiModels) { episodeUiModel ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.Margin.medium)
                    .padding(bottom = Dimens.Margin.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimens.Margin.small)
                    ) {
                        val episodeNumberText = string(textRes = episodeUiModel.episodeNumber)
                        Text(
                            text = episodeNumberText,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (episodeUiModel.title != episodeNumberText) {
                            Text(text = episodeUiModel.title, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    Text(
                        text = episodeUiModel.firstAirDate,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                RadioButton(
                    enabled = episodeUiModel.released,
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

@Preview
@Composable
private fun DetailsEpisodesModalPreview() {
    CineScoutTheme {
        DetailsEpisodesModalContent(
            uiModel = DetailsSeasonUiModelSample.BreakingBad_s2_OneEpisodeWatched,
            addToHistory = {}
        )
    }
}
