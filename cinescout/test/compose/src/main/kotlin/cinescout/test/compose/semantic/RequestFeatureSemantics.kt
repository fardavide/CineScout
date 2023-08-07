package cinescout.test.compose.semantic

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.onNodeWithTag
import cinescout.design.TestTag
import cinescout.resources.R.string
import cinescout.test.compose.util.onNodeWithText

context(ComposeUiTest)
class RequestFeatureSemantics {

    fun screen() = onNodeWithTag(TestTag.RequestFeature)
    fun title() = onNodeWithText(string.report_request_feature)
}

context(ComposeUiTest)
fun <T> RequestFeatureSemantics(block: context(RequestFeatureSemantics) () -> T): T =
    with(RequestFeatureSemantics(), block)
