package cinescout.report.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.report.domain.model.BugReportForm
import io.ktor.http.encodeURLParameter
import org.koin.core.annotation.Factory

interface BuildGitHubBugReportLink {

    operator fun invoke(form: BugReportForm): String

    companion object {

        const val Url = "https://github.com/fardavide/CineScout/issues/new?labels=bug&title=%1\$s&body=%2\$s"
    }
}

@Factory
internal class RealBuildGitHubBugReportLink(
    private val formatBugReport: FormatBugReport
) : BuildGitHubBugReportLink {

    override fun invoke(form: BugReportForm): String = BuildGitHubBugReportLink.Url.format(
        form.title.encodeURLParameter(),
        formatBugReport(form).body.encodeURLParameter()
    )
}

@CineScoutTestApi
class FakeBuildGitHubBugReportLink : BuildGitHubBugReportLink {

    override fun invoke(form: BugReportForm): String =
        BuildGitHubBugReportLink.Url.format(form.title, form.description)
}
