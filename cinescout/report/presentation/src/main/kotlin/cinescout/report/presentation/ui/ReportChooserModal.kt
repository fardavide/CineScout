package cinescout.report.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.ui.Modal
import cinescout.report.domain.model.ReportLinks
import cinescout.utils.compose.rememberOpenUrlHandler

@Composable
internal fun ReportChooserModal(links: ReportLinks, onDismiss: () -> Unit) {
    Modal(onDismiss = onDismiss) {
        ReportChooserContent(links, dismiss = onDismiss)
    }
}

@Composable
private fun ReportChooserContent(links: ReportLinks, dismiss: () -> Unit) {
    val openUrlHandler = rememberOpenUrlHandler()
    Column(
        modifier = Modifier.padding(Dimens.Margin.medium),
        verticalArrangement = Arrangement.spacedBy(Dimens.Margin.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "To send your feedback you need a GitHub account.")
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TextButton(
                    onClick = {
                        openUrlHandler.open(links.mailto)
                        dismiss()
                    }
                ) {
                    Text(text = "Send email instead", style = MaterialTheme.typography.labelSmall)
                }
                Button(
                    onClick = {
                        openUrlHandler.open(links.github)
                        dismiss()
                    }
                ) {
                    Text("Send with GitHub")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReportChooserModalPreview() {
    CineScoutTheme {
        ReportChooserContent(
            links = ReportLinks(github = "github", mailto = "mailto"),
            dismiss = {}
        )
    }
}
