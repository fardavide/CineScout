package cinescout.test.mock

import org.koin.core.module.Module

@MockAppBuilderDsl
class MockAppRuleBuilder internal constructor() {

    private val modules: MutableList<Module> = mutableListOf()

    fun newInstall() {
        modules += TestSqlDriverModule
    }

    internal fun build() = MockAppRule(
        modules = modules
    )
}

internal expect val TestSqlDriverModule: Module
