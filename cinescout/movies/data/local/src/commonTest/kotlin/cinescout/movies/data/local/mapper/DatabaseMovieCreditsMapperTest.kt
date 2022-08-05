package cinescout.movies.data.local.mapper

import cinescout.database.FindCastByMovieId
import cinescout.database.FindCrewByMovieId
import cinescout.movies.domain.testdata.MovieCreditsTestData
import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseMovieCreditsMapperTest {

    private val mapper = DatabaseMovieCreditsMapper()

    @Test
    fun `maps credits`() {
        // given
        val credits = MovieCreditsTestData.Inception
        val databaseCast = listOf(
            FindCastByMovieId(
                character = credits.cast[0].character,
                name = credits.cast[0].person.name,
                personId = credits.cast[0].person.tmdbId.toDatabaseId(),
                profileImagePath = credits.cast[0].person.profileImage.orNull()?.path
            ),
            FindCastByMovieId(
                character = credits.cast[1].character,
                name = credits.cast[1].person.name,
                personId = credits.cast[1].person.tmdbId.toDatabaseId(),
                profileImagePath = credits.cast[1].person.profileImage.orNull()?.path
            )
        )
        val databaseCrew = listOf(
            FindCrewByMovieId(
                job = credits.crew[0].job,
                name = credits.crew[0].person.name,
                personId = credits.crew[0].person.tmdbId.toDatabaseId(),
                profileImagePath = credits.crew[0].person.profileImage.orNull()?.path
            )
        )

        // when
        val result = mapper.toCredits(credits.movieId, databaseCast, databaseCrew)

        // then
        assertEquals(credits, result)
    }
}
