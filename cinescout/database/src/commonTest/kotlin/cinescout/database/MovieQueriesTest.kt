package cinescout.database

import cinescout.database.mapper.groupAsMoviesWithRating
import cinescout.database.testdata.DatabaseGenreTestData
import cinescout.database.testdata.DatabaseKeywordTestData
import cinescout.database.testdata.DatabaseMovieCastMemberTestData
import cinescout.database.testdata.DatabaseMovieCrewMemberTestData
import cinescout.database.testdata.DatabaseMovieGenreTestData
import cinescout.database.testdata.DatabaseMovieKeywordTestData
import cinescout.database.testdata.DatabaseMovieTestData
import cinescout.database.testdata.DatabaseMovieWithRatingTestData
import cinescout.database.testdata.DatabasePersonTestData
import cinescout.database.testdata.DatabaseSuggestedMovieTestData
import cinescout.database.testutil.DatabaseTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MovieQueriesTest : DatabaseTest() {

    private val genreQueries = database.genreQueries
    private val keywordQueries = database.keywordQueries
    private val likedMovieQueries = database.likedMovieQueries
    private val movieCastMemberQueries = database.movieCastMemberQueries
    private val movieCrewMemberQueries = database.movieCrewMemberQueries
    private val movieGenreQueries = database.movieGenreQueries
    private val movieKeywordQueries = database.movieKeywordQueries
    private val movieQueries = database.movieQueries
    private val movieRatingQueries = database.movieRatingQueries
    private val personQueries = database.personQueries
    private val suggestedMovieQueries = database.suggestedMovieQueries
    private val watchlistQueries = database.watchlistQueries

    @Test
    fun insertAndFindMovies() {
        // given
        val movie = DatabaseMovieTestData.Inception

        // when
        movieQueries.insertMovie(
            backdropPath = movie.backdropPath,
            posterPath = movie.posterPath,
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
        val movie = DatabaseMovieTestData.Inception

        // when
        movieQueries.insertMovie(
            backdropPath = movie.backdropPath,
            posterPath = movie.posterPath,
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
        val movie = DatabaseMovieTestData.Inception

        // when
        movieQueries.insertMovie(
            backdropPath = movie.backdropPath,
            posterPath = movie.posterPath,
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
        val movie = DatabaseMovieWithRatingTestData.Inception
        val expected = listOf(movie)

        // when
        movieQueries.insertMovie(
            backdropPath = movie.backdropPath,
            posterPath = movie.posterPath,
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

    @Test
    fun insertAndFindMovieGenres() {
        // given
        val genre1 = DatabaseGenreTestData.Action
        val genre2 = DatabaseGenreTestData.Adventure
        val movieGenre1 = DatabaseMovieGenreTestData.Action
        val movieGenre2 = DatabaseMovieGenreTestData.Adventure
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
        val result = movieQueries.findGenresByMovieId(DatabaseMovieTestData.Inception.tmdbId).executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun insertAndFindMovieKeywords() {
        // given
        val keyword1 = DatabaseKeywordTestData.Corruption
        val keyword2 = DatabaseKeywordTestData.DrugAddiction
        val movieKeyword1 = DatabaseMovieKeywordTestData.Corruption
        val movieKeyword2 = DatabaseMovieKeywordTestData.DrugAddiction
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
        val result = movieQueries.findKeywordsByMovieId(DatabaseMovieTestData.Inception.tmdbId).executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun suggestedMoviesAreSortedByAffinity() {
        // given
        val expected = listOf(
            DatabaseMovieTestData.Inception,
            DatabaseMovieTestData.TheWolfOfWallStreet,
            DatabaseMovieTestData.War
        )
        val movie1 = DatabaseMovieTestData.Inception
        val movie2 = DatabaseMovieTestData.TheWolfOfWallStreet
        val movie3 = DatabaseMovieTestData.War
        val suggestedMovie1 = DatabaseSuggestedMovieTestData.Inception
        val suggestedMovie2 = DatabaseSuggestedMovieTestData.TheWolfOfWallStreet
        val suggestedMovie3 = DatabaseSuggestedMovieTestData.War

        // when
        for (movie in listOf(movie2, movie1, movie3)) {
            movieQueries.insertMovie(
                backdropPath = movie.backdropPath,
                posterPath = movie.posterPath,
                ratingAverage = movie.ratingAverage,
                ratingCount = movie.ratingCount,
                releaseDate = movie.releaseDate,
                title = movie.title,
                tmdbId = movie.tmdbId
            )
        }
        for (suggestedMovie in listOf(suggestedMovie2, suggestedMovie1, suggestedMovie3)) {
            suggestedMovieQueries.insertSuggestion(
                tmdbId = suggestedMovie.tmdbId,
                affinity = suggestedMovie.affinity
            )
        }
        val result = movieQueries.findAllSuggested().executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun suggestedMoviesFiltersLikedAndDislikedMovies() {
        // given
        val expected = listOf(
            DatabaseMovieTestData.Inception
        )
        val movie1 = DatabaseMovieTestData.Inception
        val movie2 = DatabaseMovieTestData.TheWolfOfWallStreet
        val movie3 = DatabaseMovieTestData.War
        val suggestedMovie1 = DatabaseSuggestedMovieTestData.Inception
        val suggestedMovie2 = DatabaseSuggestedMovieTestData.TheWolfOfWallStreet
        val suggestedMovie3 = DatabaseSuggestedMovieTestData.War

        // when
        for (movie in listOf(movie2, movie1, movie3)) {
            movieQueries.insertMovie(
                backdropPath = movie.backdropPath,
                posterPath = movie.posterPath,
                ratingAverage = movie.ratingAverage,
                ratingCount = movie.ratingCount,
                releaseDate = movie.releaseDate,
                title = movie.title,
                tmdbId = movie.tmdbId
            )
        }
        for (suggestedMovie in listOf(suggestedMovie2, suggestedMovie1, suggestedMovie3)) {
            suggestedMovieQueries.insertSuggestion(
                tmdbId = suggestedMovie.tmdbId,
                affinity = suggestedMovie.affinity
            )
        }
        likedMovieQueries.insert(movie2.tmdbId, isLiked = true)
        likedMovieQueries.insert(movie3.tmdbId, isLiked = false)
        val result = movieQueries.findAllSuggested().executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun suggestedMoviesFiltersRatedMovies() {
        // given
        val expected = listOf(
            DatabaseMovieTestData.TheWolfOfWallStreet,
            DatabaseMovieTestData.War
        )
        val movie1 = DatabaseMovieTestData.Inception
        val movie2 = DatabaseMovieTestData.TheWolfOfWallStreet
        val movie3 = DatabaseMovieTestData.War
        val suggestedMovie1 = DatabaseSuggestedMovieTestData.Inception
        val suggestedMovie2 = DatabaseSuggestedMovieTestData.TheWolfOfWallStreet
        val suggestedMovie3 = DatabaseSuggestedMovieTestData.War

        // when
        for (movie in listOf(movie2, movie1, movie3)) {
            movieQueries.insertMovie(
                backdropPath = movie.backdropPath,
                posterPath = movie.posterPath,
                ratingAverage = movie.ratingAverage,
                ratingCount = movie.ratingCount,
                releaseDate = movie.releaseDate,
                title = movie.title,
                tmdbId = movie.tmdbId
            )
        }
        for (suggestedMovie in listOf(suggestedMovie2, suggestedMovie1, suggestedMovie3)) {
            suggestedMovieQueries.insertSuggestion(
                tmdbId = suggestedMovie.tmdbId,
                affinity = suggestedMovie.affinity
            )
        }
        movieRatingQueries.insertRating(movie1.tmdbId, rating = 9.0)
        val result = movieQueries.findAllSuggested().executeAsList()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun suggestedMoviesFiltersMoviesInWatchlist() {
        // given
        val expected = listOf(
            DatabaseMovieTestData.Inception,
            DatabaseMovieTestData.War
        )
        val movie1 = DatabaseMovieTestData.Inception
        val movie2 = DatabaseMovieTestData.TheWolfOfWallStreet
        val movie3 = DatabaseMovieTestData.War
        val suggestedMovie1 = DatabaseSuggestedMovieTestData.Inception
        val suggestedMovie2 = DatabaseSuggestedMovieTestData.TheWolfOfWallStreet
        val suggestedMovie3 = DatabaseSuggestedMovieTestData.War

        // when
        for (movie in listOf(movie2, movie1, movie3)) {
            movieQueries.insertMovie(
                backdropPath = movie.backdropPath,
                posterPath = movie.posterPath,
                ratingAverage = movie.ratingAverage,
                ratingCount = movie.ratingCount,
                releaseDate = movie.releaseDate,
                title = movie.title,
                tmdbId = movie.tmdbId
            )
        }
        for (suggestedMovie in listOf(suggestedMovie2, suggestedMovie1, suggestedMovie3)) {
            suggestedMovieQueries.insertSuggestion(
                tmdbId = suggestedMovie.tmdbId,
                affinity = suggestedMovie.affinity
            )
        }
        watchlistQueries.insertWatchlist(movie2.tmdbId)
        val result = movieQueries.findAllSuggested().executeAsList()

        // then
        assertEquals(expected, result)
    }
}
