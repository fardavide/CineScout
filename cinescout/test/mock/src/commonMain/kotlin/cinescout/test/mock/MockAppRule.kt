package cinescout.test.mock

import cinescout.test.mock.builder.MockAppBuilderDsl
import cinescout.test.mock.builder.MockAppConfigBuilder
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.koin.test.KoinTest

@MockAppBuilderDsl
@Deprecated(
    "Use from junit4 artifact",
    ReplaceWith("MockAppRule", "cinescout.test.mock.junit4.MockAppRule")
)
fun MockAppRule(block: MockAppConfigBuilder.() -> Unit = {}): MockAppRule =
    MockAppRule(MockAppConfigApplier(config = MockAppConfigBuilder().apply(block).build()))

class MockAppRule internal constructor(
    private val configApplier: MockAppConfigApplier
) : TestWatcher(), KoinTest {

    operator fun invoke(block: MockAppConfigBuilder.() -> Unit) {
        val config = MockAppConfigBuilder().apply(block).build()
        configApplier.apply(config = config)
    }

    override fun starting(description: Description) {
        configApplier.setup()
    }

    override fun finished(description: Description) {
        configApplier.teardown()
    }
}
