package cinescout.report.presentation.action

import cinescout.report.presentation.state.ReportBugState

internal sealed interface ReportBugAction {

    @JvmInline
    value class FocusChanged(val field: Field) : ReportBugAction {

        enum class Field {
            Description,
            ExpectedBehavior,
            Steps,
            Title
        }
    }

    data object Submit : ReportBugAction

    @JvmInline
    value class ValidateFields(val state: ReportBugState) : ReportBugAction
}
