import Rating.Negative
import Rating.Positive
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
import movies.Movie
import movies.MovieRepository
import org.koin.dsl.module
import stats.StatRepository
import kotlin.text.RegexOption.IGNORE_CASE
import Test.Movie.War as War_movie

val domainMockModule = module {
    factory<MovieRepository> { MockMovieRepository() }
    factory<StatRepository> { MockStatRepository() }
}

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
        actors: Collection<Name>,
        genres: Collection<Name>,
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
                    movie.genres.any { regex in it.s } ||
                    movie.actors.any { regex in it.s }
            }
        }
    }
}

internal class MockStatRepository : StatRepository {

    private val ratedMovies = mutableMapOf<Movie, Rating>()
    private val topActors = mutableMapOf<Name, Int>()
    private val topGenres = mutableMapOf<Name, Int>()
    private val topYears = mutableMapOf<FiveYearRange, Int>()

    override suspend fun topActors(limit: UInt): Collection<Name> =
        topActors.takeLast(limit)

    override suspend fun topGenres(limit: UInt): Collection<Name> =
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

        val AlfieAllen = Name("Alfie Allen")
        val BradPitt = Name("Brad Pitt")
        val BruceWillis = Name("Bruce Willis")

        val ChiwetelEjiofor = Name("Chiwetel Ejiofor")
        val ChristophWaltz = Name("Christoph Waltz")
        val CliveOwen = Name("Clive Owen")
        val CrispinGlover = Name("Crispin Glover")

        val DenzelWashington = Name("Denzel Washington")

        val EllenPage = Name("Ellen Page")
        val EthanSuplee = Name("Ethan Suplee")

        val ForestWhitaker = Name("Forest Whitaker")

        val GaryOldman = Name("Gary Oldman")

        val HrithikRoshan = Name("Hrithik Roshan")

        val JessicaAlba = Name("Jessica Alba")
        val JamieFoxx = Name("Jamie Foxx")
        val JenniferJasonLeigh = Name("Jennifer Jason Leigh")
        val JohnnyDepp = Name("Johnny Depp")
        val JohnTravolta = Name("John Travolta")
        val JosephGordonLevitt = Name("Joseph Gordon-Levitt")

        val KeanuReeves = Name("Keanu Reeves")
        val KenWatanabe = Name("Ken Watanabe")
        val KurtRussell = Name("Kurt Russell")

        val LauraHarring = Name("Laura Harring")
        val LeeErmey = Name("Lee Ermey")
        val LeonardoDiCaprio = Name("Leonardo DiCaprio")
        val LoganLerman = Name("Logan Lerman")

        val MichaelNyqvist = Name("Michael Nyqvist")

        val NateParker = Name("Nate Parker")

        val MilaKunis = Name("Mila Kunis")

        val PaulaPatton = Name("Paula Patton")
        val PenelopeCruz = Name("Penélope Cruz")

        val RussellCrowe = Name("Russell Crowe")

        val SamuelLJackson = Name("Samuel L. Jackson")
        val ShiaLaBeouf = Name("Shia LaBeouf")

        val TigerShroff = Name("Tiger Shroff")
        val TomHardy = Name("Tom Hardy")

        val UmaThurman = Name("Uma Thurman")

        val VaaniKapoor = Name("Vaani Kapoor")
        val ValKilmer = Name("Val Kilmer")
    }

    object Genre {

        val Action = Name("Action")
        val Adventure = Name("Adventure")
        val Crime = Name("Crime")
        val Drama = Name("Drama")
        val Horror = Name("Horror")
        val Mystery = Name("Mystery")
        val ScienceFiction = Name("Science Fiction")
        val Thriller = Name("Thriller")
        val War = Name("War")
        val Western = Name("Western")
    }

    object Movie {

        val AmericanGangster = Movie(
            name = Name("American Gangster"),
            actors = setOf(DenzelWashington, RussellCrowe, ChiwetelEjiofor),
            genres = setOf(Drama, Crime),
            year = 2007u
        )
        val Blow = Movie(
            name = Name("Blow"),
            actors = setOf(JohnnyDepp, PenelopeCruz, EthanSuplee),
            genres = setOf(Crime, Drama),
            year = 2001u
        )
        val DejaVu = Movie(
            name = Name("Déjà Vu"),
            actors = setOf(DenzelWashington, PaulaPatton, ValKilmer),
            genres = setOf(Action, Thriller, ScienceFiction),
            year = 2006u
        )
        val DjangoUnchained = Movie(
            name = Name("Django Unchained"),
            actors = setOf(JamieFoxx, ChristophWaltz, LeonardoDiCaprio),
            genres = setOf(Drama, Western),
            year = 2012u
        )
        val Fury = Movie(
            name = Name("Fury"),
            actors = setOf(BradPitt, ShiaLaBeouf, LoganLerman),
            genres = setOf(Genre.War, Drama, Action),
            year = 2014u
        )
        val Inception = Movie(
            name = Name("Inception"),
            actors = setOf(LeonardoDiCaprio, JosephGordonLevitt, EllenPage, TomHardy, KenWatanabe),
            genres = setOf(Action, ScienceFiction, Adventure),
            year = 2010u
        )
        val JohnWick = Movie(
            name = Name("John Wick"),
            actors = setOf(KeanuReeves, MichaelNyqvist, AlfieAllen),
            genres = setOf(Action, Thriller),
            year = 2014u
        )
        val PulpFiction = Movie(
            name = Name("Pulp Fiction"),
            actors = setOf(JohnTravolta, SamuelLJackson, UmaThurman),
            genres = setOf(Crime, Thriller),
            year = 1994u
        )
        val SinCity = Movie(
            name = Name("Sin City"),
            actors = setOf(BruceWillis, JessicaAlba, CliveOwen),
            genres = setOf(Action, Thriller, Crime),
            year = 2005u
        )
        val TheBookOfEli = Movie(
            name = Name("The Book of Eli"),
            actors = setOf(DenzelWashington, GaryOldman, MilaKunis),
            genres = setOf(Action, Thriller, ScienceFiction),
            year = 2010u
        )
        val TheGreatDebaters = Movie(
            name = Name("The Great Debaters"),
            actors = setOf(DenzelWashington, NateParker, ForestWhitaker),
            genres = setOf(Drama),
            year = 2007u
        )
        val TheHatefulEight = Movie(
            name = Name("The Hateful Eight"),
            actors = setOf(SamuelLJackson, KurtRussell, JenniferJasonLeigh),
            genres = setOf(Crime, Drama, Mystery, Western),
            year = 2015u
        )
        val War = Movie(
            name = Name("War"),
            actors = setOf(HrithikRoshan, TigerShroff, VaaniKapoor),
            genres = setOf(Action, Thriller),
            year = 2019u
        )
        val Willard = Movie(
            name = Name("Willard"),
            actors = setOf(CrispinGlover, LeeErmey, LauraHarring),
            genres = setOf(Horror),
            year = 2003u
        )
    }
}
