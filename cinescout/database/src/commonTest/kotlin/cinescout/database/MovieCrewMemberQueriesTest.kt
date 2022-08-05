package cinescout.database

import cinescout.database.testdata.DatabaseMovieCrewMemberTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieCrewMemberQueriesTest : DatabaseTest() {

    private val queries = database.movieCrewMemberQueries

    @Test
    fun insertAndFindCrewMember() {
        // given
        val member = DatabaseMovieCrewMemberTestData.ChristopherNolan

        // when
        queries.insertCrewMember(movieId = member.movieId, personId = member.personId, job = member.job)
        val result = queries.findAllByMovieId(member.movieId).executeAsList()

        // then
        assertEquals(listOf(member), result)
    }
}
