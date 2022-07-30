package cinescout.accuount.tmdb.data.local.mapper

import cinescout.account.tmdb.domain.testdata.TmdbAccountTestData
import cinescout.database.model.DatabaseTmdbAccount
import cinescout.database.model.UniqueDatabaseId
import cinescout.database.testdata.DatabaseTmdbAccountTestData
import kotlin.test.Test
import kotlin.test.assertEquals

class TmdbAccountMapperTest {

    private val mapper = TmdbAccountMapper()

    @Test
    fun map() {
        // given
        val expected = TmdbAccountTestData.Account
        val input = DatabaseTmdbAccount(id = UniqueDatabaseId, DatabaseTmdbAccountTestData.Username)

        // when
        val result = mapper.toTmdbAccount(input)

        // then
        assertEquals(expected, result)
    }
}
