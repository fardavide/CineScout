package cinescout.report.domain.usecase

import cinescout.CineScoutTestApi
import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.toMailtoUrl
import org.koin.core.annotation.Factory

internal interface BuildMailtoBugReportLink {

    operator fun invoke(form: BugReportForm): String
}

@Factory
internal class RealBuildMailtoBugReportLink(
    private val encodeBugReport: EncodeBugReport
) : BuildMailtoBugReportLink {

    override fun invoke(form: BugReportForm): String = encodeBugReport(form).toMailtoUrl()
}

@CineScoutTestApi
internal class FakeBuildMailtoBugReportLink : BuildMailtoBugReportLink {

    override fun invoke(form: BugReportForm): String =
        BugReportForm.MailtoUrl.format(form.title, form.description)
}
