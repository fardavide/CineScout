import movies.MovieRepository
import stats.StatRepository
import Test.Movie.Blow
import Test.Movie.PulpFiction
import Test.Movie.Willard
import Test.Actor.JohnnyDepp
import Test.Actor.DenzelWashington
import Rating.*
import Test.Actor.CrispinGlover
import Test.Actor.EthanSuplee
import Test.Actor.JohnTravolta
import Test.Actor.LauraHarring
import Test.Actor.LeeErmey
import Test.Actor.PenelopeCruz
import Test.Actor.SamuelLJackson
import Test.Actor.UmaThurman
import Test.Genre.Crime
import Test.Genre.Drama
import Test.Genre.Horror
import Test.Genre.Thriller
import Test.Genre.War
import movies.Movie

import org.koin.dsl.module

val domainMockModule = module {
    factory<MovieRepository> { MockMovieRepository() }
    factory<StatRepository> { MockStatRepository() }
}

internal class MockMovieRepository : MovieRepository

internal class MockStatRepository : StatRepository {

    private val ratedMovies = mutableMapOf<Movie, Rating>()
    private val topActors = mutableSetOf<Name>()
    private val topGenres = mutableSetOf<Name>()
    private val topYears = mutableSetOf<FiveYearRange>()

    override suspend fun topActors(limit: UInt): Collection<Name> =
        topActors.take(limit.toInt())

    override suspend fun topGenres(limit: UInt): Collection<Name> =
        topGenres.take(limit.toInt())

    override suspend fun topYears(limit: UInt): Collection<FiveYearRange> =
        topYears.take(limit.toInt())

    override suspend fun ratedMovies(): Collection<Pair<Movie, Rating>> =
        ratedMovies.toList()

    override suspend fun rate(movie: Movie, rating: Rating) {
        ratedMovies += movie to rating
        topActors += movie.actors
        topGenres += movie.genres
        topYears += FiveYearRange(forYear = movie.year)
    }
}

internal class StubStatRepository : StatRepository {

    override suspend fun topActors(limit: UInt): Collection<Name> =
        setOf(
            JohnnyDepp,
            DenzelWashington,
        ).take(limit.toInt())

    override suspend fun topGenres(limit: UInt): Collection<Name> =
        setOf(
            War,
            Horror
        ).take(limit.toInt())

    override suspend fun topYears(limit: UInt): Collection<FiveYearRange> =
        setOf(
            FiveYearRange(2020u),
            FiveYearRange(2015u)
        ).take(limit.toInt())

    override suspend fun ratedMovies(): Collection<Pair<Movie, Rating>> =
        setOf(
            Blow to Positive,
            PulpFiction to Positive,
            Willard to Negative
        )

    override suspend fun rate(movie: Movie, rating: Rating) {
        TODO("Not yet implemented")
    }
}

object Test {

    object Actor {

        val CrispinGlover = Name("Crispin Glover")
        val DenzelWashington = Name("Denzel Washington")
        val EthanSuplee = Name("Ethan Suplee")
        val JohnnyDepp = Name("Johnny Depp")
        val JohnTravolta = Name("John Travolta")
        val LauraHarring = Name("Laura Harring")
        val LeeErmey = Name("Lee Ermey")
        val PenelopeCruz = Name("Penélope Cruz")
        val SamuelLJackson = Name("Samuel L. Jackson")
        val UmaThurman = Name("Uma Thurman")
    }

    object Genre {

        val Crime = Name("Crime")
        val Drama = Name("Drama")
        val Horror = Name("Horror")
        val Thriller = Name("Thriller")
        val War = Name("War")
    }

    object Movie {

        val Blow = Movie(
            name = Name("Blow"),
            actors = setOf(JohnnyDepp, PenelopeCruz, EthanSuplee),
            genres = setOf(Crime, Drama),
            year = 2001u
        )
        val PulpFiction = Movie(
            name = Name("Pulp Fiction"),
            actors = setOf(JohnTravolta, SamuelLJackson, UmaThurman),
            genres = setOf(Crime, Thriller),
            year = 1994u
        )
        val Willard = Movie(
            name = Name("Willard"),
            actors = setOf(CrispinGlover, LeeErmey, LauraHarring),
            genres = setOf(Horror),
            year = 2003u
        )
    }
}
