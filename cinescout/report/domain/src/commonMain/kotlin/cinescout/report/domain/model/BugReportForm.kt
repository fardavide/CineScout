package cinescout.report.domain.model

data class BugReportForm(
    val title: String,
    val description: String,
    val steps: String,
    val expectedBehavior: String
)
