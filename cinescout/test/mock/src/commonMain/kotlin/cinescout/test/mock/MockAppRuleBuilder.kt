package cinescout.test.mock

import cinescout.movies.domain.model.Movie
import org.koin.core.module.Module
import org.koin.dsl.module
import store.StoreOwner
import store.test.MockStoreOwner

@MockAppBuilderDsl
class MockAppRuleBuilder internal constructor() {

    private var forYouItems: List<Movie> = emptyList()
    private val modules: MutableList<Module> = mutableListOf()
    private var shouldDisableForYouHint = false

    fun disableForYouHint() {
        shouldDisableForYouHint = true
    }

    fun forYou(block: ForYouBuilder.() -> Unit) {
        forYouItems = ForYouBuilder().apply(block).items
    }

    fun newInstall() {
        modules += TestSqlDriverModule
    }

    fun updatedCache() {
        modules += module {
            single<StoreOwner> { MockStoreOwner().updated() }
        }
    }

    internal fun build() = MockAppRuleDelegate(
        forYouItems = forYouItems,
        modules = modules,
        shouldDisableForYouHint = shouldDisableForYouHint
    )
}

internal data class MockAppRuleDelegate(
    val forYouItems: List<Movie>,
    val modules: List<Module>,
    val shouldDisableForYouHint: Boolean
)

internal expect val TestSqlDriverModule: Module
