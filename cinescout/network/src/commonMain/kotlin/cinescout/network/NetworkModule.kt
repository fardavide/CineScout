package cinescout.network

import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkModule = module {

    factory(NetworkQualifier.BaseHttpClient) {
        CineScoutClient()
    }
}

object NetworkQualifier {
    val BaseHttpClient = named("Base Http client")
}
