package cinescout.account.trakt.data.local.mapper

import cinescout.account.trakt.domain.testData.TraktAccountTestData
import cinescout.database.model.DatabaseTraktAccount
import cinescout.database.model.UniqueDatabaseId
import cinescout.database.testdata.DatabaseTraktAccountTestData
import kotlin.test.Test
import kotlin.test.assertEquals

class TraktAccountMapperTest {

    private val mapper = TraktAccountMapper()

    @Test
    fun map() {
        // given
        val expected = TraktAccountTestData.Account
        val input = DatabaseTraktAccount(
            id = UniqueDatabaseId,
            gravatarHash = DatabaseTraktAccountTestData.GravatarHash,
            username = DatabaseTraktAccountTestData.Username
        )

        // when
        val result = mapper.toTraktAccount(input)

        // then
        assertEquals(expected, result)
    }
}
