package cinescout.report.domain.model

data class BugReportForm(
    val title: String,
    val description: String,
    val steps: String,
    val expectedBehavior: String
)

data class FormattedBugReportForm(
    val title: String,
    val body: String
)
