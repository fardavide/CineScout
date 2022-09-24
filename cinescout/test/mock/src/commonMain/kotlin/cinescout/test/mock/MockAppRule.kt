package cinescout.test.mock

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import org.koin.test.KoinTest

@MockAppBuilderDsl
fun MockAppRule(block: MockAppRuleBuilder.() -> Unit): MockAppRule =
    MockAppRule(delegate = MockAppRuleBuilder().apply(block).build())

class MockAppRule internal constructor(
    private val delegate: MockAppRuleDelegate
) : TestRule, KoinTest {

    override fun apply(base: Statement, description: Description): Statement {
        getKoin().loadModules(delegate.modules)
        return base
    }
}
