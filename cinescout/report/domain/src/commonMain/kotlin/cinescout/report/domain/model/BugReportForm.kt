package cinescout.report.domain.model

data class BugReportForm(
    val title: String,
    val description: String,
    val steps: String,
    val expectedBehavior: String
) {
    companion object {

        const val GitHubUrl = "https://github.com/fardavide/CineScout/issues/new?labels=bug&title=%1\$s&body=%2\$s"
        const val MailtoUrl = "mailto:fardavide@gmail.com?subject=%1\$s&body=%2\$s"
    }
}

internal data class FormattedBugReportForm(
    val title: String,
    val body: String
)

internal data class EncodedBugReportForm(
    val title: String,
    val body: String
)

internal fun EncodedBugReportForm.toGitHubUrl(): String = BugReportForm.GitHubUrl.format(title, body)
internal fun EncodedBugReportForm.toMailtoUrl(): String = BugReportForm.MailtoUrl.format(title, body)
