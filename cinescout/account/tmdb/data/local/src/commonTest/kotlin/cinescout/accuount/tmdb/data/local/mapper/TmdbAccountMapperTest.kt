package cinescout.accuount.tmdb.data.local.mapper

import cinescout.account.domain.sample.AccountSample
import cinescout.account.tmdb.data.local.mapper.TmdbAccountMapper
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
        val expected = AccountSample.Tmdb
        val input = DatabaseTmdbAccount(
            id = UniqueDatabaseId,
            gravatarHash = DatabaseTmdbAccountTestData.GravatarHash,
            username = DatabaseTmdbAccountTestData.Username
        )

        // when
        val result = mapper.toTmdbAccount(input)

        // then
        assertEquals(expected, result)
    }
}
