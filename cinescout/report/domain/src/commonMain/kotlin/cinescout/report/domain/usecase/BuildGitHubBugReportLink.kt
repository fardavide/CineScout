package cinescout.report.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.report.domain.model.BugReportForm
import org.koin.core.annotation.Factory
import java.net.URLEncoder
import java.nio.charset.Charset

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
        URLEncoder.encode(form.title, Charset.defaultCharset()),
        URLEncoder.encode(formatBugReport(form).body, Charset.defaultCharset())
    )

}

@CineScoutTestApi
class FakeBuildGitHubBugReportLink : BuildGitHubBugReportLink {

    override fun invoke(form: BugReportForm): String = BuildGitHubBugReportLink.Url.format(
        URLEncoder.encode(form.title, Charset.defaultCharset()),
        URLEncoder.encode(form.description, Charset.defaultCharset())
    )
}
