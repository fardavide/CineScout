package cinescout.database

import cinescout.database.testdata.DatabasePersonTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PersonQueriesTest : DatabaseTest() {

    private val personQueries = database.personQueries

    @Test
    fun insertAndFindPerson() {
        // given
        val person = DatabasePersonTestData.LeonardoDiCaprio

        // when
        personQueries.insertPerson(
            name = person.name,
            profileImagePath = person.profileImagePath,
            tmdbId = person.tmdbId
        )
        val result = personQueries.findById(person.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(person, result)
    }
}
