package cinescout.database

import cinescout.database.mapper.groupAsMoviesWithRating
import cinescout.database.sample.DatabaseMovieCastMemberSample
import cinescout.database.sample.DatabaseMovieCrewMemberSample
import cinescout.database.sample.DatabaseMovieSample
import cinescout.database.sample.DatabaseScreenplayGenreSample
import cinescout.database.sample.DatabaseScreenplayKeywordSample
import cinescout.database.testdata.DatabaseGenreTestData
import cinescout.database.testdata.DatabaseKeywordTestData
import cinescout.database.testdata.DatabaseMovieWithRatingSample
import cinescout.database.testdata.DatabasePersonTestData
import cinescout.database.testutil.DatabaseTest
import org.junit.jupiter.api.Assertions.assertEquals

class MovieQueriesTest : DatabaseTest() {

    private val genreQueries get() = database.genreQueries
    private val keywordQueries get() = database.keywordQueries
    private val likedMovieQueries get() = database.likedMovieQueries
    private val movieCastMemberQueries get() = database.movieCastMemberQueries
    private val movieCrewMemberQueries get() = database.movieCrewMemberQueries
    private val movieGenreQueries get() = database.movieGenreQueries
    private val movieKeywordQueries get() = database.movieKeywordQueries
    private val movieQueries get() = database.movieQueries
    private val movieRatingQueries get() = database.movieRatingQueries
    private val personQueries get() = database.personQueries

    @Test
    fun insertAndFindMovies() {
        // given
        val movie = DatabaseMovieSample.Inception

        // when
        movieQueries.insertMovie(
            overview = movie.overview,
            ratingAverage = movie.ratingAverage,
            ratingCount = movie.ratingCount,
            releaseDate = movie.releaseDate,
            title = movie.title,
            tmdbId = movie.tmdbId
        )
        val result = movieQueries.findById(movie.tmdbId).executeAsOneOrNull()

        // then
        assertEquals(movie, result)
    }

    @Test
    fun insertAndFindAllDislikedMovies() {
        // given
        val movie = DatabaseMovieSample.Inception

        // when
        movieQueries.insertMovie(
            overview = movie.overview,
            ratingAverage = movie.ratingAverage,
            ratingCount = movie.ratingCount,
            releaseDate = movie.releaseDate,
            title = movie.title,
            tmdbId = movie.tmdbId
        )
        likedMovieQueries.insert(movie.tmdbId, isLiked = false)
        val result = movieQueries.findAllDisliked().executeAsList()

        // then
        assertEquals(listOf(movie), result)
    }

    @Test
    fun insertAndFindAllLikedMovies() {
        // given
        val movie = DatabaseMovieSample.Inception

        // when
        movieQueries.insertMovie(
            overview = movie.overview,
            ratingAverage = movie.ratingAverage,
            ratingCount = movie.ratingCount,
            releaseDate = movie.releaseDate,
            title = movie.title,
            tmdbId = movie.tmdbId
        )
        likedMovieQueries.insert(movie.tmdbId, isLiked = true)
        val result = movieQueries.findAllLiked().executeAsList()

        // then
        assertEquals(listOf(movie), result)
    }

    @Test
    fun insertAndFindAllMovieRatings() {
        // given
        val movie = DatabaseMovieWithRatingSample.Inception
        val expected = listOf(movie)

        // when
        movieQueries.insertMovie(
            overview = movie.overview,
            ratingAverage = movie.ratingAverage,
            ratingCount = movie.ratingCount,
            releaseDate = movie.releaseDate,
            title = movie.title,
            tmdbId = movie.tmdbId
        )
        movieRatingQueries.insertRating(tmdbId = movie.tmdbId, rating = movie.personalRating)
        val result = movieQueries.findAllWithPersonalRating().executeAsList().groupAsMoviesWithRating()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun insertAndFindMovieCast() {
        // given
        val person1 = DatabasePersonTestData.LeonardoDiCaprio
        val person2 = DatabasePersonTestData.JosephGordonLevitt
        val cast1 = DatabaseMovieCastMemberSample.LeonardoDiCaprio
        val cast2 = DatabaseMovieCastMemberSample.JosephGordonLevitt
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
            character = cast1.character,
            memberOrder = cast1.memberOrder
        )
        movieCastMemberQueries.insertCastMember(
            movieId = cast2.movieId,
            personId = cast2.personId,
            character = cast2.character,
            memberOrder = cast2.memberOrder
        )
        val result = movieQueries.findCastByMovieId(DatabaseMovieSample.Inception.tmdbId).executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun insertAndFindMovieCrew() {
        // given
        val person = DatabasePersonTestData.ChristopherNolan
        val crew = DatabaseMovieCrewMemberSample.ChristopherNolan
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
            job = crew.job,
            memberOrder = crew.memberOrder
        )
        val result = movieQueries.findCrewByMovieId(DatabaseMovieSample.Inception.tmdbId).executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun insertAndFindMovieGenres() {
        // given
        val genre1 = DatabaseGenreTestData.Action
        val genre2 = DatabaseGenreTestData.Adventure
        val movieGenre1 = DatabaseScreenplayGenreSample.Action
        val movieGenre2 = DatabaseScreenplayGenreSample.Adventure
        val expected = listOf(
            FindGenresByMovieId(
                genreId = genre2.tmdbId,
                name = genre2.name
            ),
            FindGenresByMovieId(
                genreId = genre1.tmdbId,
                name = genre1.name
            )
        )

        // when
        genreQueries.insertGenre(
            tmdbId = genre1.tmdbId,
            name = genre1.name
        )
        genreQueries.insertGenre(
            tmdbId = genre2.tmdbId,
            name = genre2.name
        )
        movieGenreQueries.insertGenre(
            movieId = movieGenre1.movieId,
            genreId = movieGenre1.genreId
        )
        movieGenreQueries.insertGenre(
            movieId = movieGenre2.movieId,
            genreId = movieGenre2.genreId
        )
        val result = movieQueries.findGenresByMovieId(DatabaseMovieSample.Inception.tmdbId).executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun insertAndFindMovieKeywords() {
        // given
        val keyword1 = DatabaseKeywordTestData.Corruption
        val keyword2 = DatabaseKeywordTestData.DrugAddiction
        val movieKeyword1 = DatabaseScreenplayKeywordSample.Corruption
        val movieKeyword2 = DatabaseScreenplayKeywordSample.DrugAddiction
        val expected = listOf(
            FindKeywordsByMovieId(
                genreId = keyword1.tmdbId,
                name = keyword1.name
            ),
            FindKeywordsByMovieId(
                genreId = keyword2.tmdbId,
                name = keyword2.name
            )
        )

        // when
        keywordQueries.insertKeyword(
            tmdbId = keyword1.tmdbId,
            name = keyword1.name
        )
        keywordQueries.insertKeyword(
            tmdbId = keyword2.tmdbId,
            name = keyword2.name
        )
        movieKeywordQueries.insertKeyword(
            movieId = movieKeyword1.movieId,
            keywordId = movieKeyword1.keywordId
        )
        movieKeywordQueries.insertKeyword(
            movieId = movieKeyword2.movieId,
            keywordId = movieKeyword2.keywordId
        )
        val result = movieQueries.findKeywordsByMovieId(DatabaseMovieSample.Inception.tmdbId).executeAsList()

        // then
        assertEquals(expected, result)
    }
}
