package cinescout.details.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.Modal
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.history.domain.usecase.AddToHistory
import cinescout.resources.R.string

@Composable
internal fun AddToHistoryModal(itemTitle: String, actions: AddToHistoryModal.Actions) {
    Modal(onDismiss = actions.dismiss) {
        Column(
            modifier = Modifier.padding(vertical = Dimens.Margin.small, horizontal = Dimens.Margin.small),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(string.details_add_item, itemTitle),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.size(Dimens.Margin.small))
            Text(text = stringResource(string.details_add_to_history_now), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.size(Dimens.Margin.small))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = {
                        actions.addToHistory()
                        actions.dismiss()
                    }
                ) {
                    Text(text = stringResource(string.details_add))
                }
            }
        }
    }
}

object AddToHistoryModal {

    data class Actions(
        val dismiss: () -> Unit,
        val addToHistory: () -> Unit
    ) {

        companion object {

            val Empty = Actions(
                dismiss = {},
                addToHistory = {}
            )
        }
    }

    data class Params(
        val itemTitle: String,
        val addToHistoryParams: AddToHistory.Params
    )
}

@Preview
@Composable
private fun AddToHistoryModalPreview() {
    CineScoutTheme {
        AddToHistoryModal(
            itemTitle = ScreenplayDetailsUiModelSample.Inception.title,
            actions = AddToHistoryModal.Actions.Empty
        )
    }
}
