package cinescout.network

import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkModule = module {

    factory(NetworkQualifier.BaseHttpClient) {
        CineScoutClient()
    }
    factory {
        DualSourceCall(
            isFirstSourceLinked = get(NetworkQualifier.IsFirstSourceLinked),
            isSecondSourceLinked = get(NetworkQualifier.IsSecondSourceLinked)
        )
    }
}

object NetworkQualifier {
    val BaseHttpClient = named("Base Http client")
    val IsFirstSourceLinked = named("Is first source linked")
    val IsSecondSourceLinked = named("Is second source linked")
}
