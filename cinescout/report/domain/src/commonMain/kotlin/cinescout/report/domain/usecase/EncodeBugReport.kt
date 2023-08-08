package cinescout.report.domain.usecase

import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.EncodedBugReportForm
import cinescout.report.domain.model.FormattedBugReportForm
import io.ktor.http.encodeURLParameter
import org.koin.core.annotation.Factory

internal interface EncodeBugReport {

    operator fun invoke(report: FormattedBugReportForm): EncodedBugReportForm

    operator fun invoke(report: BugReportForm): EncodedBugReportForm
}

@Factory
internal class RealEncodeBugReport(
    private val formatBugReport: FormatBugReport
) : EncodeBugReport {

    override fun invoke(report: FormattedBugReportForm) = EncodedBugReportForm(
        title = report.title.encodeURLParameter(),
        body = report.body.encodeURLParameter()
    )

    override fun invoke(report: BugReportForm): EncodedBugReportForm = this(formatBugReport(report))
}
