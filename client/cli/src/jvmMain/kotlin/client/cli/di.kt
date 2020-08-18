package client.cli

import client.cli.controller.GetSuggestionsController
import client.cli.controller.MenuController
import client.cli.controller.RateMovieController
import client.cli.controller.SearchController
import client.clientModule
import org.koin.core.KoinComponent
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import kotlin.reflect.KClass

val cliClientModule = module {

    factory { MenuController() }
    factory { SearchController() }
    factory { RateMovieController() }
    factory { GetSuggestionsController() }

} + clientModule

@Suppress("NOTHING_TO_INLINE")
internal inline fun <T : Any> KoinComponent.inject(
    kClass: KClass<T>,
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { getKoin().get(kClass, qualifier, parameters) }
