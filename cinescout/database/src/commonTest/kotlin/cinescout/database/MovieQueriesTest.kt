package cinescout.database

import cinescout.database.testdata.DatabaseMovieCastMemberTestData
import cinescout.database.testdata.DatabaseMovieCrewMemberTestData
import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testdata.DatabasePersonTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieQueriesTest : DatabaseTest() {

    private val movieCastMemberQueries = database.movieCastMemberQueries
    private val movieCrewMemberQueries = database.movieCrewMemberQueries
    private val movieQueries = database.movieQueries
    private val movieRatingQueries = database.movieRatingQueries
    private val personQueries = database.personQueries

    @Test
    fun insertAndFindMovies() {
        // given
        val movie = DatabaseMovieTestData.Inception

        // when
        movieQueries.insertMovie(tmdbId = movie.tmdbId, releaseDate = movie.releaseDate, title = movie.title)
        val result = movieQueries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(movie, result)
    }

    @Test
    fun insertAndFindAllMovieRatings() {
        // given
        val movie = DatabaseMovieTestData.Inception
        val rating = 8.0
        val expected = listOf(
            FindAllWithRating(
                tmdbId = movie.tmdbId,
                rating = rating,
                releaseDate = movie.releaseDate,
                title = movie.title
            )
        )

        // when
        movieQueries.insertMovie(tmdbId = movie.tmdbId, releaseDate = movie.releaseDate, title = movie.title)
        movieRatingQueries.insertRating(tmdbId = movie.tmdbId, rating = rating)
        val result = movieQueries.findAllWithRating().executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun insertAndFindMovieCast() {
        // given
        val person1 = DatabasePersonTestData.LeonardoDiCaprio
        val person2 = DatabasePersonTestData.JosephGordonLevitt
        val cast1 = DatabaseMovieCastMemberTestData.LeonardoDiCaprio
        val cast2 = DatabaseMovieCastMemberTestData.JosephGordonLevitt
        val expected = listOf(
            FindCastByMovieId(
                character = cast1.character,
                name = person1.name,
                personId = person1.tmdbId,
                profileImagePath = person1.profileImagePath
            ),
            FindCastByMovieId(
                character = cast2.character,
                name = person2.name,
                personId = person2.tmdbId,
                profileImagePath = person2.profileImagePath
            )
        )

        // when
        personQueries.insertPerson(
            tmdbId = person1.tmdbId,
            name = person1.name,
            profileImagePath = person1.profileImagePath
        )
        personQueries.insertPerson(
            tmdbId = person2.tmdbId,
            name = person2.name,
            profileImagePath = person2.profileImagePath
        )
        movieCastMemberQueries.insertCastMember(
            movieId = cast1.movieId,
            personId = cast1.personId,
            character = cast1.character
        )
        movieCastMemberQueries.insertCastMember(
            movieId = cast2.movieId,
            personId = cast2.personId,
            character = cast2.character
        )
        val result = movieQueries.findCastByMovieId(DatabaseMovieTestData.Inception.tmdbId).executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun insertAndFindMovieCrew() {
        // given
        val person = DatabasePersonTestData.ChristopherNolan
        val crew = DatabaseMovieCrewMemberTestData.ChristopherNolan
        val expected = listOf(
            FindCrewByMovieId(
                job = crew.job,
                name = person.name,
                personId = person.tmdbId,
                profileImagePath = person.profileImagePath
            )
        )

        // when
        personQueries.insertPerson(
            tmdbId = person.tmdbId,
            name = person.name,
            profileImagePath = person.profileImagePath
        )
        movieCrewMemberQueries.insertCrewMember(
            movieId = crew.movieId,
            personId = crew.personId,
            job = crew.job
        )
        val result = movieQueries.findCrewByMovieId(DatabaseMovieTestData.Inception.tmdbId).executeAsList()

        // then
        assertEquals(expected, result)
    }
}
