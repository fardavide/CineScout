import Test.Actor.AlfieAllen
import Test.Actor.BradPitt
import Test.Actor.BruceWillis
import Test.Actor.ChiwetelEjiofor
import Test.Actor.ChristophWaltz
import Test.Actor.CliveOwen
import Test.Actor.CrispinGlover
import Test.Actor.DenzelWashington
import Test.Actor.EllenPage
import Test.Actor.EthanSuplee
import Test.Actor.ForestWhitaker
import Test.Actor.GaryOldman
import Test.Actor.HrithikRoshan
import Test.Actor.JamieFoxx
import Test.Actor.JenniferJasonLeigh
import Test.Actor.JessicaAlba
import Test.Actor.JohnTravolta
import Test.Actor.JohnnyDepp
import Test.Actor.JosephGordonLevitt
import Test.Actor.KeanuReeves
import Test.Actor.KenWatanabe
import Test.Actor.KurtRussell
import Test.Actor.LauraHarring
import Test.Actor.LeeErmey
import Test.Actor.LeonardoDiCaprio
import Test.Actor.LoganLerman
import Test.Actor.MichaelNyqvist
import Test.Actor.MilaKunis
import Test.Actor.NateParker
import Test.Actor.PaulaPatton
import Test.Actor.PenelopeCruz
import Test.Actor.RussellCrowe
import Test.Actor.SamuelLJackson
import Test.Actor.ShiaLaBeouf
import Test.Actor.TigerShroff
import Test.Actor.TomHardy
import Test.Actor.UmaThurman
import Test.Actor.VaaniKapoor
import Test.Actor.ValKilmer
import Test.Genre.Action
import Test.Genre.Adventure
import Test.Genre.Crime
import Test.Genre.Drama
import Test.Genre.Horror
import Test.Genre.Mystery
import Test.Genre.ScienceFiction
import Test.Genre.Thriller
import Test.Genre.War
import Test.Genre.Western
import Test.Movie.AmericanGangster
import Test.Movie.Blow
import Test.Movie.DejaVu
import Test.Movie.DjangoUnchained
import Test.Movie.Fury
import Test.Movie.Inception
import Test.Movie.JohnWick
import Test.Movie.PulpFiction
import Test.Movie.SinCity
import Test.Movie.TheBookOfEli
import Test.Movie.TheGreatDebaters
import Test.Movie.TheHatefulEight
import Test.Movie.Willard
import entities.Actor
import entities.FiveYearRange
import entities.Genre
import entities.Name
import entities.Rating
import entities.Rating.Negative
import entities.Rating.Positive
import entities.TmdbId
import entities.movies.Movie
import entities.movies.MovieRepository
import entities.stats.StatRepository
import org.koin.dsl.module
import kotlin.text.RegexOption.IGNORE_CASE
import Test.Movie.War as War_movie

val domainMockMovieModule = module {
    factory<MovieRepository> { MockMovieRepository() }
}

val domainMockStatModule = module {
    single<StatRepository> { MockStatRepository() }
}

val domainMockModule = domainMockMovieModule + domainMockStatModule

internal class MockMovieRepository : MovieRepository {

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
        TheGreatDebaters,
        TheHatefulEight,
        War_movie,
        Willard,
    )

    override suspend fun discover(
        actors: Collection<Actor>,
        genres: Collection<Genre>,
        years: FiveYearRange?
    ) = allMovies.filter {
        (years == null || it.year in years.range) &&
            (genres.isEmpty() || genres.intersect(it.genres).isNotEmpty()) &&
            (actors.isEmpty() || actors.intersect(it.actors).isNotEmpty())
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

internal class MockStatRepository : StatRepository {

    private val ratedMovies = mutableMapOf<Movie, Rating>()
    private val topActors = mutableMapOf<Actor, Int>()
    private val topGenres = mutableMapOf<Genre, Int>()
    private val topYears = mutableMapOf<FiveYearRange, Int>()

    override suspend fun topActors(limit: UInt): Collection<Actor> =
        topActors.takeLast(limit)

    override suspend fun topGenres(limit: UInt): Collection<Genre> =
        topGenres.takeLast(limit)

    override suspend fun topYears(limit: UInt): Collection<FiveYearRange> =
        topYears.takeLast(limit)

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

    private fun <K> Map<K, Int>.takeLast(limit: UInt): Collection<K> =
        entries.sortedBy { it.value }.takeLast(limit.toInt()).map { it.key }

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
        TODO("Not yet implemented")
    }
}

object Test {

    object Actor {

        val AlfieAllen = Actor(id = TmdbId("Alfie Allen".hashCode()), name = Name("Alfie Allen"))
        val BradPitt = Actor(id = TmdbId("Brad Pitt".hashCode()), name = Name("Brad Pitt"))
        val BruceWillis = Actor(id = TmdbId("Bruce Willis".hashCode()), name = Name("Bruce Willis"))

        val ChiwetelEjiofor = Actor(id = TmdbId(5294), name = Name("Chiwetel Ejiofor"))
        val ChristophWaltz = Actor(id = TmdbId("Christoph Waltz".hashCode()), name = Name("Christoph Waltz"))
        val CliveOwen = Actor(id = TmdbId("Clive Owen".hashCode()), name = Name("Clive Owen"))
        val CrispinGlover = Actor(id = TmdbId("Crispin Glover".hashCode()), name = Name("Crispin Glover"))

        val DenzelWashington = Actor(id = TmdbId(5292), name = Name("Denzel Washington"))

        val EllenPage = Actor(id = TmdbId(27578), name = Name("Ellen Page"))
        val EthanSuplee = Actor(id = TmdbId("Ethan Suplee".hashCode()), name = Name("Ethan Suplee"))

        val ForestWhitaker = Actor(id = TmdbId("Forest Whitaker".hashCode()), name = Name("Forest Whitaker"))

        val GaryOldman = Actor(id = TmdbId(64), name = Name("Gary Oldman"))

        val HrithikRoshan = Actor(id = TmdbId("Hrithik Roshan".hashCode()), name = Name("Hrithik Roshan"))

        val JessicaAlba = Actor(id = TmdbId("Jessica Alba".hashCode()), name = Name("Jessica Alba"))
        val JamieFoxx = Actor(id = TmdbId("Jamie Foxx".hashCode()), name = Name("Jamie Foxx"))
        val JenniferJasonLeigh = Actor(id = TmdbId("Jennifer Jason Leigh".hashCode()), name = Name("Jennifer Jason Leigh"))
        val JohnnyDepp = Actor(id = TmdbId("Johnny Depp".hashCode()), name = Name("Johnny Depp"))
        val JohnTravolta = Actor(id = TmdbId("John Travolta".hashCode()), name = Name("John Travolta"))
        val JosephGordonLevitt = Actor(id = TmdbId(24045), name = Name("Joseph Gordon-Levitt"))

        val KeanuReeves = Actor(id = TmdbId("Keanu Reeves".hashCode()), name = Name("Keanu Reeves"))
        val KenWatanabe = Actor(id = TmdbId(3899), name = Name("Ken Watanabe"))
        val KurtRussell = Actor(id = TmdbId("Kurt Russell".hashCode()), name = Name("Kurt Russell"))

        val LauraHarring = Actor(id = TmdbId("Laura Harring".hashCode()), name = Name("Laura Harring"))
        val LeeErmey = Actor(id = TmdbId("Lee Ermey".hashCode()), name = Name("Lee Ermey"))
        val LeonardoDiCaprio = Actor(id = TmdbId(6193), name = Name("Leonardo DiCaprio"))
        val LoganLerman = Actor(id = TmdbId("Logan Lerman".hashCode()), name = Name("Logan Lerman"))

        val MichaelNyqvist = Actor(id = TmdbId("Michael Nyqvist".hashCode()), name = Name("Michael Nyqvist"))

        val NateParker = Actor(id = TmdbId("Nate Parker".hashCode()), name = Name("Nate Parker"))

        val MilaKunis = Actor(id = TmdbId(18973), name = Name("Mila Kunis"))

        val PaulaPatton = Actor(id = TmdbId("Paula Patton".hashCode()), name = Name("Paula Patton"))
        val PenelopeCruz = Actor(id = TmdbId("Penélope Cruz".hashCode()), name = Name("Penélope Cruz"))

        val RussellCrowe = Actor(id = TmdbId(934), name = Name("Russell Crowe"))

        val SamuelLJackson = Actor(id = TmdbId("Samuel L. Jackson".hashCode()), name = Name("Samuel L. Jackson"))
        val ShiaLaBeouf = Actor(id = TmdbId("Shia LaBeouf".hashCode()), name = Name("Shia LaBeouf"))

        val TigerShroff = Actor(id = TmdbId("Tiger Shroff".hashCode()), name = Name("Tiger Shroff"))
        val TomHardy = Actor(id = TmdbId(2524), name = Name("Tom Hardy"))

        val UmaThurman = Actor(id = TmdbId("Uma Thurman".hashCode()), name = Name("Uma Thurman"))

        val VaaniKapoor = Actor(id = TmdbId("Vaani Kapoor".hashCode()), name = Name("Vaani Kapoor"))
        val ValKilmer = Actor(id = TmdbId("Val Kilmer".hashCode()), name = Name("Val Kilmer"))
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
            id = TmdbId(0),
            name = Name("American Gangster"),
            actors = setOf(DenzelWashington, RussellCrowe, ChiwetelEjiofor),
            genres = setOf(Drama, Crime),
            year = 2007u
        )
        val Blow = Movie(
            id = TmdbId(0),
            name = Name("Blow"),
            actors = setOf(JohnnyDepp, PenelopeCruz, EthanSuplee),
            genres = setOf(Crime, Drama),
            year = 2001u
        )
        val DejaVu = Movie(
            id = TmdbId(7551),
            name = Name("Déjà Vu"),
            actors = setOf(DenzelWashington, PaulaPatton, ValKilmer),
            genres = setOf(Action, Thriller, ScienceFiction),
            year = 2006u
        )
        val DjangoUnchained = Movie(
            id = TmdbId(0),
            name = Name("Django Unchained"),
            actors = setOf(JamieFoxx, ChristophWaltz, LeonardoDiCaprio),
            genres = setOf(Drama, Western),
            year = 2012u
        )
        val Fury = Movie(
            id = TmdbId(0),
            name = Name("Fury"),
            actors = setOf(BradPitt, ShiaLaBeouf, LoganLerman),
            genres = setOf(Genre.War, Drama, Action),
            year = 2014u
        )
        val Inception = Movie(
            id = TmdbId(27205),
            name = Name("Inception"),
            actors = setOf(LeonardoDiCaprio, JosephGordonLevitt, EllenPage, TomHardy, KenWatanabe),
            genres = setOf(Action, ScienceFiction, Adventure),
            year = 2010u
        )
        val JohnWick = Movie(
            id = TmdbId(0),
            name = Name("John Wick"),
            actors = setOf(KeanuReeves, MichaelNyqvist, AlfieAllen),
            genres = setOf(Action, Thriller),
            year = 2014u
        )
        val PulpFiction = Movie(
            id = TmdbId(0),
            name = Name("Pulp Fiction"),
            actors = setOf(JohnTravolta, SamuelLJackson, UmaThurman),
            genres = setOf(Crime, Thriller),
            year = 1994u
        )
        val SinCity = Movie(
            id = TmdbId(0),
            name = Name("Sin City"),
            actors = setOf(BruceWillis, JessicaAlba, CliveOwen),
            genres = setOf(Action, Thriller, Crime),
            year = 2005u
        )
        val TheBookOfEli = Movie(
            id = TmdbId(20504),
            name = Name("The Book of Eli"),
            actors = setOf(DenzelWashington, GaryOldman, MilaKunis),
            genres = setOf(Action, Thriller, ScienceFiction),
            year = 2010u
        )
        val TheGreatDebaters = Movie(
            id = TmdbId(14047),
            name = Name("The Great Debaters"),
            actors = setOf(DenzelWashington, NateParker, ForestWhitaker),
            genres = setOf(Drama),
            year = 2007u
        )
        val TheHatefulEight = Movie(
            id = TmdbId(0),
            name = Name("The Hateful Eight"),
            actors = setOf(SamuelLJackson, KurtRussell, JenniferJasonLeigh),
            genres = setOf(Crime, Drama, Mystery, Western),
            year = 2015u
        )
        val War = Movie(
            id = TmdbId(0),
            name = Name("War"),
            actors = setOf(HrithikRoshan, TigerShroff, VaaniKapoor),
            genres = setOf(Action, Thriller),
            year = 2019u
        )
        val Willard = Movie(
            id = TmdbId(10929),
            name = Name("Willard"),
            actors = setOf(CrispinGlover, LeeErmey, LauraHarring),
            genres = setOf(Horror),
            year = 2003u
        )
    }
}
