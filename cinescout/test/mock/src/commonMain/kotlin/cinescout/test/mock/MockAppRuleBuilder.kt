package cinescout.test.mock

import org.koin.core.module.Module

@MockAppBuilderDsl
class MockAppRuleBuilder internal constructor() {

    private val modules: MutableList<Module> = mutableListOf()

    fun newInstall() {
        modules += TestSqlDriverModule
    }

    internal fun build() = MockAppRuleDelegate(
        modules = modules
    )
}

internal data class MockAppRuleDelegate(
    val modules: List<Module>
)

internal expect val TestSqlDriverModule: Module
