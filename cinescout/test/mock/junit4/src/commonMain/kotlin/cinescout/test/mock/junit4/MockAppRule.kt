package cinescout.test.mock.junit4

import cinescout.CineScoutTestApi
import cinescout.test.mock.MockAppConfigApplier
import cinescout.test.mock.MockEngines
import cinescout.test.mock.builder.MockAppBuilderDsl
import cinescout.test.mock.builder.MockAppConfigBuilder
import io.ktor.client.engine.mock.MockEngine
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@CineScoutTestApi
@MockAppBuilderDsl
fun MockAppRule(block: MockAppConfigBuilder.() -> Unit = {}): MockAppRule {
    val tmdbMockEngine = MockEngines.tmdb()
    val traktMockEngine = MockEngines.trakt()
    val mockAppConfigBuilder = MockAppConfigBuilder(
        tmdbMockEngine = tmdbMockEngine,
        traktMockEngine = traktMockEngine
    )
    return MockAppRule(
        MockAppConfigApplier(
            config = mockAppConfigBuilder.apply(block).build(),
            tmdbMockEngine = tmdbMockEngine,
            traktMockEngine = traktMockEngine
        ),
        tmdbMockEngine = tmdbMockEngine,
        traktMockEngine = traktMockEngine
    )
}

class MockAppRule internal constructor(
    private val configApplier: MockAppConfigApplier,
    val tmdbMockEngine: MockEngine,
    val traktMockEngine: MockEngine
) : TestWatcher() {

    operator fun invoke(block: MockAppConfigBuilder.() -> Unit) {
        val config = MockAppConfigBuilder(
            tmdbMockEngine = tmdbMockEngine,
            traktMockEngine = traktMockEngine
        ).apply(block).build()
        configApplier.apply(config = config)
    }

    override fun starting(description: Description) {
        configApplier.setup()
    }

    override fun finished(description: Description) {
        configApplier.teardown()
    }
}
