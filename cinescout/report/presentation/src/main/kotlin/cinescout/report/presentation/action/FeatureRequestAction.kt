package cinescout.report.presentation.action

import cinescout.report.presentation.model.FeatureRequestField

internal sealed interface FeatureRequestAction {

    @JvmInline
    value class FocusChanged(val field: FeatureRequestField) : FeatureRequestAction

    data class Submit(
        val alternativeSolutions: String,
        val description: String,
        val title: String
    ) : FeatureRequestAction

    data class ValidateField(val field: FeatureRequestField, val text: String) : FeatureRequestAction
}
