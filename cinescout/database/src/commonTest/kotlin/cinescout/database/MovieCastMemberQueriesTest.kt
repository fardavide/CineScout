package cinescout.database

import cinescout.database.testdata.DatabaseMovieCastMemberTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieCastMemberQueriesTest : DatabaseTest() {

    private val queries = database.movieCastMemberQueries

    @Test
    fun insertAndFindOneCastMember() {
        // given
        val member = DatabaseMovieCastMemberTestData.LeonardoDiCaprio

        // when
        queries.insertCastMember(
            movieId = member.movieId,
            personId = member.personId,
            character = member.character,
            memberOrder = member.memberOrder
        )
        val result = queries.findAllByMovieId(member.movieId).executeAsList()

        // then
        assertEquals(listOf(member), result)
    }

    @Test
    fun insertAndFindTwoCastMember() {
        // given
        val member1 = DatabaseMovieCastMemberTestData.LeonardoDiCaprio
        val member2 = DatabaseMovieCastMemberTestData.JosephGordonLevitt

        // when
        queries.insertCastMember(
            movieId = member1.movieId,
            personId = member1.personId,
            character = member1.character,
            memberOrder = member1.memberOrder
        )
        queries.insertCastMember(
            movieId = member2.movieId,
            personId = member2.personId,
            character = member2.character,
            memberOrder = member2.memberOrder
        )
        val result = queries.findAllByMovieId(member1.movieId).executeAsList()

        // then
        assertEquals(listOf(member1, member2), result)
    }
}
