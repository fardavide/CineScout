package domain

import domain.Test.Actor.AlfieAllen
import domain.Test.Actor.BradPitt
import domain.Test.Actor.BruceWillis
import domain.Test.Actor.ChiwetelEjiofor
import domain.Test.Actor.ChristophWaltz
import domain.Test.Actor.CliveOwen
import domain.Test.Actor.CrispinGlover
import domain.Test.Actor.DenzelWashington
import domain.Test.Actor.EllenPage
import domain.Test.Actor.EthanSuplee
import domain.Test.Actor.ForestWhitaker
import domain.Test.Actor.GaryOldman
import domain.Test.Actor.HrithikRoshan
import domain.Test.Actor.JamieFoxx
import domain.Test.Actor.JenniferJasonLeigh
import domain.Test.Actor.JessicaAlba
import domain.Test.Actor.JohnTravolta
import domain.Test.Actor.JohnnyDepp
import domain.Test.Actor.JosephGordonLevitt
import domain.Test.Actor.KeanuReeves
import domain.Test.Actor.KenWatanabe
import domain.Test.Actor.KurtRussell
import domain.Test.Actor.LauraHarring
import domain.Test.Actor.LeeErmey
import domain.Test.Actor.LeonardoDiCaprio
import domain.Test.Actor.LoganLerman
import domain.Test.Actor.MichaelNyqvist
import domain.Test.Actor.MilaKunis
import domain.Test.Actor.NateParker
import domain.Test.Actor.PaulaPatton
import domain.Test.Actor.PenelopeCruz
import domain.Test.Actor.RussellCrowe
import domain.Test.Actor.SamuelLJackson
import domain.Test.Actor.ShiaLaBeouf
import domain.Test.Actor.TigerShroff
import domain.Test.Actor.TomHardy
import domain.Test.Actor.UmaThurman
import domain.Test.Actor.VaaniKapoor
import domain.Test.Actor.ValKilmer
import domain.Test.Genre.Action
import domain.Test.Genre.Adventure
import domain.Test.Genre.Crime
import domain.Test.Genre.Drama
import domain.Test.Genre.Horror
import domain.Test.Genre.Mystery
import domain.Test.Genre.ScienceFiction
import domain.Test.Genre.Thriller
import domain.Test.Genre.War
import domain.Test.Genre.Western
import domain.Test.Movie.AmericanGangster
import domain.Test.Movie.Blow
import domain.Test.Movie.DejaVu
import domain.Test.Movie.DjangoUnchained
import domain.Test.Movie.Fury
import domain.Test.Movie.Inception
import domain.Test.Movie.JohnWick
import domain.Test.Movie.PulpFiction
import domain.Test.Movie.SinCity
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheEqualizer
import domain.Test.Movie.TheGreatDebaters
import domain.Test.Movie.TheHatefulEight
import domain.Test.Movie.Willard
import entities.Actor
import entities.FiveYearRange
import entities.Genre
import entities.Name
import entities.Poster
import entities.Rating
import entities.Rating.Negative
import entities.Rating.Positive
import entities.TmdbId
import entities.movies.DiscoverParams
import entities.movies.Movie
import entities.movies.MovieRepository
import entities.stats.StatRepository
import org.koin.dsl.module
import util.unsupported
import kotlin.text.RegexOption.IGNORE_CASE
import domain.Test.Movie.War as War_movie

val domainMockMovieModule = module {
    factory<MovieRepository> { MockMovieRepository() }
}

val domainMockStatModule = module {
    single<StatRepository> { MockStatRepository() }
}

val domainMockModule = domainMockMovieModule + domainMockStatModule

class MockMovieRepository : MovieRepository {

    private val allMovies = setOf(
        AmericanGangster,
        Blow,
        DejaVu,
        DjangoUnchained,
        Fury,
        Inception,
        JohnWick,
        PulpFiction,
        SinCity,
        TheBookOfEli,
        TheEqualizer,
        TheGreatDebaters,
        TheHatefulEight,
        War_movie,
        Willard,
    )

    override suspend fun find(id: TmdbId) =
        allMovies.find { it.id == id }

    override suspend fun discover(params: DiscoverParams) = allMovies.filter { movie ->
        (params.year == null || params.year == movie.year.toInt()) &&
            (params.genre in movie.genres.map { it.id }) &&
            (params.actor in movie.actors.map { it.id })
    }

    override suspend fun search(query: String): Collection<Movie> {
        return if (query.isBlank()) emptySet()
        else {
            val regex = query.trim()
                .replace("[ ]+".toRegex(), " ")
                .toRegex(IGNORE_CASE)
            allMovies.filter { movie ->
                regex in movie.name.s ||
                    movie.genres.any { regex in it.name.s } ||
                    movie.actors.any { regex in it.name.s }
            }
        }
    }
}

class MockStatRepository : StatRepository {

    private val ratedMovies = mutableMapOf<Movie, Rating>()
    private val topActors = mutableMapOf<Actor, Int>()
    private val topGenres = mutableMapOf<Genre, Int>()
    private val topYears = mutableMapOf<FiveYearRange, Int>()

    override suspend fun topActors(limit: UInt): Collection<Actor> =
        topActors.takeTop(limit)

    override suspend fun topGenres(limit: UInt): Collection<Genre> =
        topGenres.takeTop(limit)

    override suspend fun topYears(limit: UInt): Collection<FiveYearRange> =
        topYears.takeTop(limit)

    override suspend fun ratedMovies(): Collection<Pair<Movie, Rating>> =
        ratedMovies.toList()

    override suspend fun rate(movie: Movie, rating: Rating) {
        val prevWeight = ratedMovies[movie]?.weight ?: 0
        ratedMovies += movie to rating
        updateStatsFor(movie, rating.weight - prevWeight)
    }

    private fun updateStatsFor(movie: Movie, weight: Int) {
        topActors *= movie.actors to weight
        topGenres *= movie.genres to weight
        topYears += FiveYearRange(forYear = movie.year) to weight
    }

    private fun <K> Map<K, Int>.takeTop(limit: UInt): Collection<K> =
        entries.sortedByDescending { it.value }.take(limit.toInt()).map { it.key }

    private operator fun <T, C: Collection<T>> MutableMap<T, Int>.timesAssign(pair: Pair<C, Int>) {
        val (elements, weight) = pair
        for (element in elements) {
            plusAssign(element to weight)
        }
    }

    private operator fun <T> MutableMap<T, Int>.plusAssign(pair: Pair<T, Int>) {
        val (element, weight) = pair
        val prev = get(element) ?: 0
        set(element, prev + weight)
    }

    private operator fun <T, C: Collection<T>> MutableMap<T, Int>.divAssign(pair: Pair<C, Int>) {
        val (elements, weight) = pair
        for (element in elements) {
            minusAssign(element to weight)
        }
    }

    private operator fun <T> MutableMap<T, Int>.minusAssign(pair: Pair<T, Int>) {
        val (element, weight) = pair
        val prev = get(element) ?: 0
        set(element, prev - weight)
    }
}

internal class StubStatRepository : StatRepository {

    override suspend fun topActors(limit: UInt): Collection<Actor> =
        setOf(
            JohnnyDepp,
            DenzelWashington,
        ).take(limit.toInt())

    override suspend fun topGenres(limit: UInt): Collection<Genre> =
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
        unsupported
    }
}

object Test {

    object Actor {

        val AlfieAllen = Actor(id = TmdbId(71586), name = Name("Alfie Allen"))
        val BradPitt = Actor(id = TmdbId(287), name = Name("Brad Pitt"))
        val BruceWillis = Actor(id = TmdbId(62), name = Name("Bruce Willis"))

        val ChiwetelEjiofor = Actor(id = TmdbId(5294), name = Name("Chiwetel Ejiofor"))
        val ChristophWaltz = Actor(id = TmdbId(27319), name = Name("Christoph Waltz"))
        val CliveOwen = Actor(id = TmdbId(2296), name = Name("Clive Owen"))
        val CrispinGlover = Actor(id = TmdbId(1064), name = Name("Crispin Glover"))

        val DenzelWashington = Actor(id = TmdbId(5292), name = Name("Denzel Washington"))

        val EllenPage = Actor(id = TmdbId(27578), name = Name("Ellen Page"))
        val EthanSuplee = Actor(id = TmdbId(824), name = Name("Ethan Suplee"))

        val ForestWhitaker = Actor(id = TmdbId(2178), name = Name("Forest Whitaker"))

        val GaryOldman = Actor(id = TmdbId(64), name = Name("Gary Oldman"))

        val HrithikRoshan = Actor(id = TmdbId(78749), name = Name("Hrithik Roshan"))

        val JessicaAlba = Actor(id = TmdbId(56731), name = Name("Jessica Alba"))
        val JamieFoxx = Actor(id = TmdbId(134), name = Name("Jamie Foxx"))
        val JenniferJasonLeigh = Actor(id = TmdbId(10431), name = Name("Jennifer Jason Leigh"))
        val JohnnyDepp = Actor(id = TmdbId(85), name = Name("Johnny Depp"))
        val JohnTravolta = Actor(id = TmdbId(8891), name = Name("John Travolta"))
        val JosephGordonLevitt = Actor(id = TmdbId(24045), name = Name("Joseph Gordon-Levitt"))

        val KeanuReeves = Actor(id = TmdbId(6384), name = Name("Keanu Reeves"))
        val KenWatanabe = Actor(id = TmdbId(3899), name = Name("Ken Watanabe"))
        val KurtRussell = Actor(id = TmdbId(6856), name = Name("Kurt Russell"))

        val LauraHarring = Actor(id = TmdbId(15007), name = Name("Laura Harring"))
        val LeeErmey = Actor(id = TmdbId(8655), name = Name("Lee Ermey"))
        val LeonardoDiCaprio = Actor(id = TmdbId(6193), name = Name("Leonardo DiCaprio"))
        val LoganLerman = Actor(id = TmdbId(33235), name = Name("Logan Lerman"))

        val MichaelNyqvist = Actor(id = TmdbId(6283), name = Name("Michael Nyqvist"))

        val NateParker = Actor(id = TmdbId(77277), name = Name("Nate Parker"))

        val MilaKunis = Actor(id = TmdbId(18973), name = Name("Mila Kunis"))

        val PaulaPatton = Actor(id = TmdbId(52851), name = Name("Paula Patton"))
        val PenelopeCruz = Actor(id = TmdbId(955), name = Name("Penélope Cruz"))

        val RussellCrowe = Actor(id = TmdbId(934), name = Name("Russell Crowe"))

        val SamuelLJackson = Actor(id = TmdbId(2231), name = Name("Samuel L. Jackson"))
        val ShiaLaBeouf = Actor(id = TmdbId(10959), name = Name("Shia LaBeouf"))

        val TigerShroff = Actor(id = TmdbId(1338512), name = Name("Tiger Shroff"))
        val TomHardy = Actor(id = TmdbId(2524), name = Name("Tom Hardy"))

        val UmaThurman = Actor(id = TmdbId(139), name = Name("Uma Thurman"))

        val VaaniKapoor = Actor(id = TmdbId(1192903), name = Name("Vaani Kapoor"))
        val ValKilmer = Actor(id = TmdbId(5576), name = Name("Val Kilmer"))
    }

    object Genre {

        val Action = Genre(id = TmdbId(28), name = Name("Action"))
        val Adventure = Genre(id = TmdbId(12), name = Name("Adventure"))
        val Crime = Genre(id = TmdbId(80), name = Name("Crime"))
        val Drama = Genre(id = TmdbId(18), name = Name("Drama"))
        val Horror = Genre(id = TmdbId(27), name = Name("Horror"))
        val Mystery = Genre(id = TmdbId(9648), name = Name("Mystery"))
        val ScienceFiction = Genre(TmdbId(878), name = Name("Science Fiction"))
        val Thriller = Genre(id = TmdbId(53), name = Name("Thriller"))
        val War = Genre(id = TmdbId(10752), name = Name("War"))
        val Western = Genre(id = TmdbId(37), name = Name("Western"))
    }

    object Movie {

        val AmericanGangster = Movie(
            id = TmdbId(4982),
            name = Name("American Gangster"),
            poster = Poster("/8sV6nWuKczuXRt0C6EWoXqJAj6G.jpg"),
            actors = setOf(DenzelWashington, RussellCrowe, ChiwetelEjiofor),
            genres = setOf(Drama, Crime),
            year = 2007u
        )
        val Blow = Movie(
            id = TmdbId(4133),
            name = Name("Blow"),
            poster = Poster("/ii4sylRdQnLFPMCLhaER7vb0J6N.jpg"),
            actors = setOf(JohnnyDepp, PenelopeCruz, EthanSuplee),
            genres = setOf(Crime, Drama),
            year = 2001u
        )
        val DejaVu = Movie(
            id = TmdbId(7551),
            name = Name("Déjà Vu"),
            poster = Poster("/hL8W0qgoPKw7xQy7LMir2numqsP.jpg"),
            actors = setOf(DenzelWashington, PaulaPatton, ValKilmer),
            genres = setOf(Action, Thriller, ScienceFiction),
            year = 2006u
        )
        val DjangoUnchained = Movie(
            id = TmdbId(18921),
            name = Name("Django Unchained"),
            poster = Poster("/7oWY8VDWW7thTzWh3OKYRkWUlD5.jpg"),
            actors = setOf(JamieFoxx, ChristophWaltz, LeonardoDiCaprio),
            genres = setOf(Drama, Western),
            year = 2012u
        )
        val Fury = Movie(
            id = TmdbId(228150),
            name = Name("Fury"),
            poster = Poster("/pfte7wdMobMF4CVHuOxyu6oqeeA.jpg"),
            actors = setOf(BradPitt, ShiaLaBeouf, LoganLerman),
            genres = setOf(Genre.War, Drama, Action),
            year = 2014u
        )
        val Inception = Movie(
            id = TmdbId(27205),
            name = Name("Inception"),
            poster = Poster("/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg"),
            actors = setOf(LeonardoDiCaprio, JosephGordonLevitt, EllenPage, TomHardy, KenWatanabe),
            genres = setOf(Action, ScienceFiction, Adventure),
            year = 2010u
        )
        val JohnWick = Movie(
            id = TmdbId(245891),
            name = Name("John Wick"),
            poster = Poster("/h3VxEVUOoBZmo79O8RqKvyGiqmE.jpg"),
            actors = setOf(KeanuReeves, MichaelNyqvist, AlfieAllen),
            genres = setOf(Action, Thriller),
            year = 2014u
        )
        val PulpFiction = Movie(
            id = TmdbId(680),
            name = Name("Pulp Fiction"),
            poster = Poster("/plnlrtBUULT0rh3Xsjmpubiso3L.jpg"),
            actors = setOf(JohnTravolta, SamuelLJackson, UmaThurman),
            genres = setOf(Crime, Thriller),
            year = 1994u
        )
        val SinCity = Movie(
            id = TmdbId(187),
            name = Name("Sin City"),
            poster = Poster("/1Br0CXgpDIgF0ue7HVhO08bn7kn.jpg"),
            actors = setOf(BruceWillis, JessicaAlba, CliveOwen),
            genres = setOf(Action, Thriller, Crime),
            year = 2005u
        )
        val TheBookOfEli = Movie(
            id = TmdbId(20504),
            name = Name("The Book of Eli"),
            poster = Poster("/1H1y9ZiqNFaLgQiRDDZLA55PviW.jpg"),
            actors = setOf(DenzelWashington, GaryOldman, MilaKunis),
            genres = setOf(Action, Thriller, ScienceFiction),
            year = 2010u
        )
        val TheEqualizer = Movie(
            id = TmdbId(156022),
            name = Name("The Equalizer"),
            poster = Poster("/9u4yW7yPA0BQ2pv9XwiNzItwvp8.jpg"),
            actors = setOf(DenzelWashington),
            genres = setOf(Action, Crime, Thriller),
            year = 2014u
        )
        val TheGreatDebaters = Movie(
            id = TmdbId(14047),
            name = Name("The Great Debaters"),
            poster = Poster("/jxsWIZzjpaRNd0Ni4v3iISk3SRr.jpg"),
            actors = setOf(DenzelWashington, NateParker, ForestWhitaker),
            genres = setOf(Drama),
            year = 2007u
        )
        val TheHatefulEight = Movie(
            id = TmdbId(273248),
            name = Name("The Hateful Eight"),
            poster = Poster("/nZeKw2oDiODgnht9OrohB2jBhjq.jpg"),
            actors = setOf(SamuelLJackson, KurtRussell, JenniferJasonLeigh),
            genres = setOf(Crime, Drama, Mystery, Western),
            year = 2015u
        )
        val War = Movie(
            id = TmdbId(0),
            name = Name("War"),
            poster = null,
            actors = setOf(HrithikRoshan, TigerShroff, VaaniKapoor),
            genres = setOf(Action, Thriller),
            year = 2019u
        )
        val Willard = Movie(
            id = TmdbId(10929),
            name = Name("Willard"),
            poster = Poster("/6FMNo5aBX7tAiNigmFGeopPBBqh.jpg"),
            actors = setOf(CrispinGlover, LeeErmey, LauraHarring),
            genres = setOf(Horror),
            year = 2003u
        )

        private fun Poster(path: String) = Poster("https://image.tmdb.org/t/p", path)
    }
}
