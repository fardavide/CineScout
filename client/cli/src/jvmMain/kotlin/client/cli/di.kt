package client.cli

import client.clientModule
import org.koin.core.KoinComponent
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import kotlin.reflect.KClass

val cliClientModule = module {

} + clientModule

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T : Any> KoinComponent.inject(
    kClass: KClass<T>,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { getKoin().get(kClass, qualifier, parameters) }
