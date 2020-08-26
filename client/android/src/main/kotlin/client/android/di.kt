package client.android

import client.clientModule
import org.koin.dsl.module

val androidClientModule = module {

    single { AndroidNavigator() }

} + clientModule
