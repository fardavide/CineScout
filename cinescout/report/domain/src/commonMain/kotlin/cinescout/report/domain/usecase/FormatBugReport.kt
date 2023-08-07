package cinescout.report.domain.usecase

import cinescout.report.domain.model.BugReportForm
import cinescout.report.domain.model.FormattedBugReportForm
import cinescout.report.domain.usecase.FormatBugReport.Companion.BodyForm
import org.koin.core.annotation.Factory

interface FormatBugReport {

    operator fun invoke(form: BugReportForm): FormattedBugReportForm

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
internal class RealFormatBugReport : FormatBugReport {

    override fun invoke(form: BugReportForm) = FormattedBugReportForm(
        title = form.title,
        body = BodyForm.format(
            form.description,
            form.steps,
            form.expectedBehavior
        )
    )
}
