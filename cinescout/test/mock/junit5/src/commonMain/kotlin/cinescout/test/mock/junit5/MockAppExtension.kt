package cinescout.test.mock.junit5

import cinescout.test.mock.MockAppConfigApplier
import cinescout.test.mock.MockEngines
import cinescout.test.mock.builder.MockAppBuilderDsl
import cinescout.test.mock.builder.MockAppConfigBuilder
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import io.ktor.client.engine.mock.MockEngine
import org.koin.test.KoinTest

@MockAppBuilderDsl
fun MockAppExtension(block: MockAppConfigBuilder.() -> Unit = {}): MockAppExtension {
    val tmdbMockEngines = MockEngines.tmdb()
    val traktMockEngines = MockEngines.trakt()
    val mockAppConfigBuilder = MockAppConfigBuilder(
        tmdbMockEngine = tmdbMockEngines,
        traktMockEngine = traktMockEngines
    )
    return MockAppExtension(
        configApplier = MockAppConfigApplier(
            config = mockAppConfigBuilder.apply(block).build(),
            tmdbMockEngine = tmdbMockEngines,
            traktMockEngine = traktMockEngines
        ),
        tmdbMockEngine = tmdbMockEngines,
        traktMockEngine = traktMockEngines
    )
}

class MockAppExtension internal constructor(
    private val configApplier: MockAppConfigApplier,
    val tmdbMockEngine: MockEngine,
    val traktMockEngine: MockEngine
) : BeforeSpecListener, AfterSpecListener, KoinTest {

    operator fun invoke(block: MockAppConfigBuilder.() -> Unit) {
        val config = MockAppConfigBuilder(
            tmdbMockEngine = tmdbMockEngine,
            traktMockEngine = traktMockEngine
        ).apply(block).build()
        configApplier.apply(config = config)
    }

    override suspend fun beforeSpec(spec: Spec) {
        configApplier.setup()
    }

    override suspend fun afterSpec(spec: Spec) {
        configApplier.teardown()
    }
}
