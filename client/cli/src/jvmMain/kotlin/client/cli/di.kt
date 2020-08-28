package client.cli

import client.clientModule
import co.touchlab.kermit.CommonLogger
import co.touchlab.kermit.Logger
import org.koin.core.KoinComponent
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import kotlin.reflect.KClass

val cliClientModule = module {

    factory<Logger> { CommonLogger() }

} + clientModule
