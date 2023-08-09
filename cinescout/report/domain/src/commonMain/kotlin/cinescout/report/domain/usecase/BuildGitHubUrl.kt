package cinescout.report.domain.usecase

import org.koin.core.annotation.Factory

internal interface BuildGitHubUrl {

    operator fun invoke(
        label: String,
        title: String,
        body: String
    ): String

    companion object {

        const val Url = "https://github.com/fardavide/CineScout/issues/new?labels=%1\$s&title=%2\$s&body=%3\$s"
    }
}

@Factory
internal class RealBuildGitHubUrl : BuildGitHubUrl {

    override fun invoke(
        label: String,
        title: String,
        body: String
    ): String = BuildGitHubUrl.Url.format(label, title, body)
}
