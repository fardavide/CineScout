package auth.trakt

import auth.credentials.authCredentialsModule
import network.trakt.traktNetworkModule
import org.koin.dsl.module

val traktAuthModule = module {


} + traktNetworkModule + authCredentialsModule
