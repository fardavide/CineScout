package cinescout.report.domain.usecase

import org.koin.core.annotation.Factory

internal interface BuildMailtoUrl {

    operator fun invoke(title: String, body: String): String

    companion object {

        const val Url = "mailto:fardavide@gmail.com?subject=%1\$s&body=%2\$s"
    }
}

@Factory
internal class RealBuildMailtoUrl : BuildMailtoUrl {

    override fun invoke(title: String, body: String): String = BuildMailtoUrl.Url.format(title, body)
}
