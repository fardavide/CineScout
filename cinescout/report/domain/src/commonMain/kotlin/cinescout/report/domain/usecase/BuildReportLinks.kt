package cinescout.report.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.ReportLinks
import org.koin.core.annotation.Factory

interface BuildReportLinks {

    operator fun invoke(bugReportForm: BugReportForm): ReportLinks
}

@Factory
internal class RealBuildReportLinks(
    private val buildGitHubBugReportLink: BuildGitHubBugReportLink,
    private val buildMailtoBugReportLink: BuildMailtoBugReportLink
) : BuildReportLinks {

    override fun invoke(bugReportForm: BugReportForm): ReportLinks = ReportLinks(
        github = buildGitHubBugReportLink(bugReportForm),
        mailto = buildMailtoBugReportLink(bugReportForm)
    )
}

@CineScoutTestApi
class FakeBuildReportLinks : BuildReportLinks {

    override fun invoke(bugReportForm: BugReportForm): ReportLinks = ReportLinks(
        github = BugReportForm.GitHubUrl.format(bugReportForm.title, bugReportForm.description),
        mailto = BugReportForm.MailtoUrl.format(bugReportForm.title, bugReportForm.description)
    )
}
