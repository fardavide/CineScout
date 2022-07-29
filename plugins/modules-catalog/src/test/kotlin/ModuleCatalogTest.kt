import io.mockk.mockk
import org.gradle.api.Project
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ModuleCatalogTest {

    private val project: Project = mockk(relaxed = true)
    private val catalog = ModulesCatalog(project)

    @BeforeTest
    fun setup() {
        ModulesCatalog.catalog = catalog
    }

    @Test
    fun `top level module creates the right path`() {
        // given
        val expected = ":cinescout:cinescout-test"

        // when
        val result = test.module.normalizedPath

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `second level module creates the right path`() {
        // given
        val expected = ":cinescout:cinescout-test:cinescout-test-compose"

        // when
        val result = test.compose.module.normalizedPath

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `third level module creates the right path`() {
        // given
        val expected =
            ":cinescout:cinescout-auth:cinescout-auth-trakt:domain:cinescout-auth-trakt-domain"

        // when
        val result = auth.trakt.domain.module.normalizedPath

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get default sourceSet name`() {
        // given
        val expected = "commonMain"

        // when
        val result = utils.kotlin.sourceSetName

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `get overridden sourceSet name`() {
        // given
        val expected = "androidTest"

        // when
        val result = test.compose.sourceSetName

        // then
        assertEquals(expected, result)
    }
}


