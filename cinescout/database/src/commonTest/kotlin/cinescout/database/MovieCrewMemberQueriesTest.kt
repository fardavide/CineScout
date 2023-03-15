package cinescout.database

import cinescout.database.sample.DatabaseMovieCrewMemberSample
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals

class MovieCrewMemberQueriesTest : DatabaseTest() {

    private val queries get() = database.movieCrewMemberQueries

    @Test
    fun insertAndFindCrewMember() {
        // given
        val member = DatabaseMovieCrewMemberSample.ChristopherNolan

        // when
        queries.insertCrewMember(
            movieId = member.movieId,
            personId = member.personId,
            job = member.job,
            memberOrder = member.memberOrder
        )
        val result = queries.findAllByMovieId(member.movieId).executeAsList()

        // then
        assertEquals(listOf(member), result)
    }
}
