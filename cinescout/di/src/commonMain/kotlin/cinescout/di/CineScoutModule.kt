package cinescout.di

import cinescout.network.NetworkModule
import cinescout.network.trakt.TraktNetworkModule
import org.koin.dsl.module

val CineScoutModule = module {
    includes(NetworkModule, TraktNetworkModule, TraktNetworkModule)
}
