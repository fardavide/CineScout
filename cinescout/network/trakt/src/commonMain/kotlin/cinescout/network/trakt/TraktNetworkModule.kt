package cinescout.network.trakt

import org.koin.core.qualifier.named
import org.koin.dsl.module

val NetworkTraktModule = module {

    single(TraktNetworkQualifier.Client) { CineScoutTraktClient() }
}

object TraktNetworkQualifier {

    val Client = named("Trakt client")
}
