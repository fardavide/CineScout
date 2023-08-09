package cinescout.report.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.FeatureRequestForm
import cinescout.report.domain.model.ReportLinks
import org.koin.core.annotation.Factory

interface BuildReportLinks {

    operator fun invoke(bugReportForm: BugReportForm): ReportLinks

    operator fun invoke(featureRequestForm: FeatureRequestForm): ReportLinks
}

@Factory
internal class RealBuildReportLinks(
    private val buildGitHubUrl: BuildGitHubUrl,
    private val buildMailtoUrl: BuildMailtoUrl,
    private val formatGitHubBugReport: FormatGitHubBugReport,
    private val formatGitHubFeatureRequest: FormatGitHubFeatureRequest,
    private val formatMailtoBugReport: FormatMailtoBugReport,
    private val formatMailtoFeatureRequest: FormatMailtoFeatureRequest,
    private val encodeBugReport: EncodeBugReport
) : BuildReportLinks {

    override fun invoke(bugReportForm: BugReportForm): ReportLinks {
        val gitHubEncodedForm = encodeBugReport(formatGitHubBugReport(bugReportForm))
        val mailtoEncodedForm = encodeBugReport(formatMailtoBugReport(bugReportForm))
        return ReportLinks(
            github = buildGitHubUrl(label = "bug", title = gitHubEncodedForm.title, body = gitHubEncodedForm.body),
            mailto = buildMailtoUrl(title = mailtoEncodedForm.title, body = mailtoEncodedForm.body)
        )
    }

    override fun invoke(featureRequestForm: FeatureRequestForm): ReportLinks {
        val gitHubEncodedForm = encodeBugReport(formatGitHubFeatureRequest(featureRequestForm))
        val mailtoEncodedForm = encodeBugReport(formatMailtoFeatureRequest(featureRequestForm))
        return ReportLinks(
            github = buildGitHubUrl(label = "feature", title = gitHubEncodedForm.title, body = gitHubEncodedForm.body),
            mailto = buildMailtoUrl(title = mailtoEncodedForm.title, body = mailtoEncodedForm.body)
        )
    }
}

@CineScoutTestApi
class FakeBuildReportLinks : BuildReportLinks {

    override fun invoke(bugReportForm: BugReportForm): ReportLinks = ReportLinks(
        github = BuildGitHubUrl.Url.format("bug", bugReportForm.title, bugReportForm.description),
        mailto = BuildMailtoUrl.Url.format(bugReportForm.title, bugReportForm.description)
    )

    override fun invoke(featureRequestForm: FeatureRequestForm): ReportLinks = ReportLinks(
        github = BuildGitHubUrl.Url.format("feature", featureRequestForm.title, featureRequestForm.description),
        mailto = BuildMailtoUrl.Url.format(featureRequestForm.title, featureRequestForm.description)
    )
}
