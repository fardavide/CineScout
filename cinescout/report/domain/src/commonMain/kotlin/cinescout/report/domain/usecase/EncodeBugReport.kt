package cinescout.report.domain.usecase

import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.EncodedReportForm
import cinescout.report.domain.model.FormattedReportForm
import io.ktor.http.encodeURLParameter
import org.koin.core.annotation.Factory

internal interface EncodeBugReport {

    operator fun invoke(report: FormattedReportForm): EncodedReportForm

    operator fun invoke(report: BugReportForm): EncodedReportForm
}

@Factory
internal class RealEncodeBugReport(
    private val formatGitHubBugReport: FormatGitHubBugReport
) : EncodeBugReport {

    override fun invoke(report: FormattedReportForm) = EncodedReportForm(
        title = report.title.encodeURLParameter(),
        body = report.body.encodeURLParameter()
    )

    override fun invoke(report: BugReportForm): EncodedReportForm = this(formatGitHubBugReport(report))
}
