package profile.trakt.remote

import network.trakt.client
import org.koin.dsl.module
import profile.trakt.RemoteTraktProfileSource
import profile.trakt.traktProfileModule

val remoteTraktProfileModule = module {

    factory { UserService(client = get(client)) }
    factory<RemoteTraktProfileSource> { RemoteTraktProfileSourceImpl(userService = get()) }

} + traktProfileModule
