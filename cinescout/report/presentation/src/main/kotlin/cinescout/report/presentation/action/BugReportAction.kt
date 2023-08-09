package cinescout.report.presentation.action

import cinescout.report.presentation.model.BugReportField

internal sealed interface BugReportAction {

    @JvmInline
    value class FocusChanged(val field: BugReportField) : BugReportAction

    data class Submit(
        val description: String,
        val expectedBehavior: String,
        val steps: String,
        val title: String
    ) : BugReportAction

    data class ValidateField(val field: BugReportField, val text: String) : BugReportAction
}
