package cinescout.report.domain.usecase

import cinescout.report.domain.model.FeatureRequestForm
import cinescout.report.domain.model.GitHubFormattedReportForm
import org.koin.core.annotation.Factory

internal interface FormatGitHubFeatureRequest {

    operator fun invoke(form: FeatureRequestForm): GitHubFormattedReportForm

    companion object {

        val BodyForm = """
            **Description**
            %1${'$'}s

            **Alternative solutions**
            %2${'$'}s
        """.trimIndent()
    }
}

@Factory
internal class RealFormatGitHubFeatureRequest : FormatGitHubFeatureRequest {

    override fun invoke(form: FeatureRequestForm) = GitHubFormattedReportForm(
        title = form.title,
        body = FormatGitHubFeatureRequest.BodyForm.format(
            form.description,
            form.alternativeSolutions
        )
    )
}
