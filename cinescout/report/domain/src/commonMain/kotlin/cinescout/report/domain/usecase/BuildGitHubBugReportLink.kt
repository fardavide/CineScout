package cinescout.report.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.toGitHubUrl
import org.koin.core.annotation.Factory

internal interface BuildGitHubBugReportLink {

    operator fun invoke(form: BugReportForm): String
}

@Factory
internal class RealBuildGitHubBugReportLink(
    private val encodeBugReport: EncodeBugReport
) : BuildGitHubBugReportLink {

    override fun invoke(form: BugReportForm): String = encodeBugReport(form).toGitHubUrl()
}

@CineScoutTestApi
internal class FakeBuildGitHubBugReportLink : BuildGitHubBugReportLink {

    override fun invoke(form: BugReportForm): String =
        BugReportForm.GitHubUrl.format(form.title, form.description)
}
