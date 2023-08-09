package cinescout.report.domain.usecase

import cinescout.report.domain.model.FeatureRequestForm
import cinescout.report.domain.model.MailtoFormattedReportForm
import org.koin.core.annotation.Factory

internal interface FormatMailtoFeatureRequest {

    operator fun invoke(form: FeatureRequestForm): MailtoFormattedReportForm


    companion object {

        const val TitleForm = "[CineScout Feature Request] %1\$s"

        val BodyForm = """
            Description:
            %1${'$'}s

            Alternative solutions:
            %2${'$'}s
        """.trimIndent()
    }
}

@Factory
internal class RealFormatMailtoFeatureRequest : FormatMailtoFeatureRequest {

    override fun invoke(form: FeatureRequestForm) = MailtoFormattedReportForm(
        title = FormatMailtoFeatureRequest.TitleForm.format(form.title),
        body = FormatMailtoFeatureRequest.BodyForm.format(
            form.description,
            form.alternativeSolutions
        )
    )
}
