package cinescout.report.presentation.action

import cinescout.report.presentation.model.ReportBugField

internal sealed interface ReportBugAction {

    @JvmInline
    value class FocusChanged(val field: ReportBugField) : ReportBugAction

    data class Submit(
        val description: String,
        val expectedBehavior: String,
        val steps: String,
        val title: String
    ) : ReportBugAction

    data class ValidateField(val field: ReportBugField, val text: String) : ReportBugAction
}
