package cinescout.report.domain.model

internal sealed interface FormattedReportForm {
    val title: String
    val body: String
}

data class GitHubFormattedReportForm(
    override val title: String,
    override val body: String
) : FormattedReportForm

data class MailtoFormattedReportForm(
    override val title: String,
    override val body: String
) : FormattedReportForm
