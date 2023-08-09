package cinescout.report.domain.usecase

import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.GitHubFormattedReportForm
import org.koin.core.annotation.Factory

internal interface FormatGitHubBugReport {

    operator fun invoke(form: BugReportForm): GitHubFormattedReportForm

    companion object {

        val BodyForm = """
            **Description**
            %1${'$'}s

            **Steps to Reproduce**
            %2${'$'}s

            **Expected behavior**
            %3${'$'}s
        """.trimIndent()
    }
}

@Factory
internal class RealFormatGitHubBugReport : FormatGitHubBugReport {

    override fun invoke(form: BugReportForm) = GitHubFormattedReportForm(
        title = form.title,
        body = FormatGitHubBugReport.BodyForm.format(
            form.description,
            form.steps,
            form.expectedBehavior
        )
    )
}
