package cinescout.report.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.ui.BackBottomBar
import cinescout.resources.R.string

@Composable
fun ReportScreen(back: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.testTag(TestTag.Report),
        topBar = { TopBar() },
        bottomBar = { BackBottomBar(back = back) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(text = "Report Screen")
        }
    }
}

@Composable
private fun TopBar() {
    CenterAlignedTopAppBar(title = { Text(text = stringResource(id = string.report_report_bug)) })
}

@Preview
@Composable
private fun ReportScreenPreview() {
    CineScoutTheme {
        ReportScreen(back = {})
    }
}
