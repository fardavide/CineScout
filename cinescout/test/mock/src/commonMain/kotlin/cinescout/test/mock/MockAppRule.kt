package cinescout.test.mock

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.core.module.Module
import org.koin.test.KoinTest

@MockAppBuilderDsl
fun MockAppRule(block: MockAppRuleBuilder.() -> Unit): MockAppRule =
    MockAppRuleBuilder().apply(block).build()

class MockAppRule(
    private val modules: List<Module>
) : TestRule, KoinTest {

    override fun apply(base: Statement, description: Description): Statement {
        getKoin().loadModules(modules)
        return base
    }
}
