package cinescout.report.domain.usecase

import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.MailtoFormattedReportForm
import org.koin.core.annotation.Factory

internal interface FormatMailtoBugReport {

    operator fun invoke(form: BugReportForm): MailtoFormattedReportForm

    companion object {

        const val TitleForm = "[CineScout Bug Report] %1\$s"

        val BodyForm = """
            Description:
            %1${'$'}s

            Steps to Reproduce:
            %2${'$'}s

            Expected behavior:
            %3${'$'}s
        """.trimIndent()
    }
}

@Factory
internal class RealFormatMailtoBugReport : FormatMailtoBugReport {

    override fun invoke(form: BugReportForm) = MailtoFormattedReportForm(
        title = FormatMailtoBugReport.TitleForm.format(form.title),
        body = FormatMailtoBugReport.BodyForm.format(
            form.description,
            form.steps,
            form.expectedBehavior
        )
    )
}
