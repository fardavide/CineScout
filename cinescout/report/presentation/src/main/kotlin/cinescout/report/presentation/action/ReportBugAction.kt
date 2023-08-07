package cinescout.report.presentation.action

import cinescout.report.presentation.model.ReportBugField

internal sealed interface ReportBugAction {

    @JvmInline
    value class FocusChanged(val field: ReportBugField) : ReportBugAction

    data object Submit : ReportBugAction

    data object ValidateAllFields : ReportBugAction

    data class ValidateField(val field: ReportBugField, val text: String) : ReportBugAction
}
