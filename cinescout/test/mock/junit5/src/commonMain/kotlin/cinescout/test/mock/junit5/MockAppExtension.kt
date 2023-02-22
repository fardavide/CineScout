package cinescout.test.mock.junit5

import cinescout.test.mock.MockAppConfigApplier
import cinescout.test.mock.builder.MockAppBuilderDsl
import cinescout.test.mock.builder.MockAppConfigBuilder
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec

@MockAppBuilderDsl
fun MockAppExtension(block: MockAppConfigBuilder.() -> Unit = {}): MockAppExtension =
    MockAppExtension(MockAppConfigApplier(config = MockAppConfigBuilder().apply(block).build()))

class MockAppExtension internal constructor(
    private val configApplier: MockAppConfigApplier
) : BeforeSpecListener, AfterSpecListener {

    operator fun invoke(block: MockAppConfigBuilder.() -> Unit) {
        val config = MockAppConfigBuilder().apply(block).build()
        configApplier.apply(config = config)
    }

    override suspend fun beforeSpec(spec: Spec) {
        configApplier.setup()
    }

    override suspend fun afterSpec(spec: Spec) {
        configApplier.teardown()
    }
}
