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
import entities.Either
import entities.MissingCache
import entities.NetworkError
import entities.ResourceError
import entities.Right
import entities.TmdbId
import entities.left
import entities.model.Actor
import entities.model.CommunityRating
import entities.model.EmailAddress
import entities.model.FiveYearRange
import entities.model.Genre
import entities.model.Name
import entities.model.Password
import entities.model.TmdbImageUrl
import entities.model.UserRating
import entities.model.UserRating.Negative
import entities.model.UserRating.Neutral
import entities.model.UserRating.Positive
import entities.movies.DiscoverParams
import entities.movies.Movie
import entities.movies.MovieRepository
import entities.movies.SearchError
import entities.right
import entities.stats.StatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.dsl.module
import util.interval
import util.takeIfNotEmpty
import util.unsupported
import kotlin.text.RegexOption.IGNORE_CASE
import kotlin.time.seconds
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

    override suspend fun find(id: TmdbId): Either<ResourceError, Movie> =
        allMovies.find { it.id == id }?.right() ?: ResourceError.Local(MissingCache).left()

    override suspend fun discover(params: DiscoverParams): Either<NetworkError, List<Movie>> =
        allMovies.filter { movie ->
            (params.year == null || params.year == movie.year.toInt()) &&
            (params.genre in movie.genres.map { it.id }) &&
            (params.actor in movie.actors.map { it.id })
        }.right()

    override suspend fun search(query: String): Either<SearchError, List<Movie>> {
        return if (query.isBlank()) SearchError.EmptyQuery.left()
        else {
            val regex = query.trim()
                .replace("[ ]+".toRegex(), " ")
                .toRegex(IGNORE_CASE)
            allMovies.filter { movie ->
                regex in movie.name.s ||
                    movie.genres.any { regex in it.name.s } ||
                    movie.actors.any { regex in it.name.s }
            }.right()
        }
    }
}

class MockStatRepository : StatRepository {

    private val ratedMovies = mutableMapOf<Movie, UserRating>()
    private val topActors = mutableMapOf<Actor, Int>()
    private val topGenres = mutableMapOf<Genre, Int>()
    private val topYears = mutableMapOf<FiveYearRange, Int>()

    private val suggestions: MutableStateFlow<Collection<Movie>> =
        MutableStateFlow(emptyList())

    private val watchlist: MutableStateFlow<Collection<Movie>> =
        MutableStateFlow(emptyList())

    override suspend fun topActors(limit: Int): Collection<Actor> =
        topActors.takeTop(limit)

    override suspend fun topGenres(limit: Int): Collection<Genre> =
        topGenres.takeTop(limit)

    override suspend fun topYears(limit: Int): Collection<FiveYearRange> =
        topYears.takeTop(limit)

    override suspend fun ratedMovies(): Collection<Pair<Movie, UserRating>> =
        ratedMovies.toList()

    override fun rating(movie: Movie): Flow<UserRating> =
        interval(REFRESH_DELAY) {
            ratedMovies[movie] ?: Neutral
        }
    override fun watchlist(): Flow<Either<ResourceError, Collection<Movie>>> =
        watchlist.map(::Right)

    override fun isInWatchlist(movie: Movie): Flow<Boolean> =
        watchlist.map { movie in it }

    override fun suggestions(): Flow<Either<ResourceError, Collection<Movie>>> =
        suggestions.map { it.takeIfNotEmpty()?.right() ?: ResourceError.Local(MissingCache).left() }

    override suspend fun rate(movie: Movie, rating: UserRating) {
        val prevWeight = ratedMovies[movie]?.weight ?: 0
        ratedMovies += movie to rating
        updateStatsFor(movie, rating.weight - prevWeight)
    }

    override suspend fun addToWatchlist(movie: Movie) {
        watchlist.value += movie
    }

    override suspend fun removeFromWatchlist(movie: Movie) {
        watchlist.value -= movie
    }

    override suspend fun addSuggestions(movies: Collection<Movie>) {
        suggestions.value += movies
    }

    override suspend fun removeSuggestion(movie: Movie) {
        suggestions.value -= movie
    }

    private fun updateStatsFor(movie: Movie, weight: Int) {
        topActors *= movie.actors to weight
        topGenres *= movie.genres to weight
        topYears += FiveYearRange(forYear = movie.year) to weight
    }

    private fun <K> Map<K, Int>.takeTop(limit: Int): Collection<K> =
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

    private companion object {
        val REFRESH_DELAY = 1.seconds
    }
}

internal class StubStatRepository : StatRepository {

    override suspend fun topActors(limit: Int): Collection<Actor> =
        setOf(
            JohnnyDepp,
            DenzelWashington,
        ).take(limit)

    override suspend fun topGenres(limit: Int): Collection<Genre> =
        setOf(
            War,
            Horror
        ).take(limit)

    override suspend fun topYears(limit: Int): Collection<FiveYearRange> =
        setOf(
            FiveYearRange(2020u),
            FiveYearRange(2015u)
        ).take(limit)

    private val ratedMovies = mapOf(
        Blow to Positive,
        PulpFiction to Positive,
        Willard to Negative
    )
    override suspend fun ratedMovies(): Collection<Pair<Movie, UserRating>> =
        ratedMovies.toList()

    override fun rating(movie: Movie): Flow<UserRating> =
        flowOf(ratedMovies[movie] ?: Neutral)

    private val watchlist = setOf(Fury, TheBookOfEli, TheHatefulEight)
    override fun watchlist(): Flow<Either<ResourceError, Collection<Movie>>> =
        flowOf(watchlist).map(::Right)

    override fun isInWatchlist(movie: Movie): Flow<Boolean> =
        flowOf(movie in watchlist)

    override fun suggestions(): Flow<Either<ResourceError, Collection<Movie>>> =
        flowOf(listOf(Blow, TheBookOfEli)).map(::Right)

    override suspend fun rate(movie: Movie, rating: UserRating) {
        unsupported
    }

    override suspend fun addToWatchlist(movie: Movie) {
        unsupported
    }

    override suspend fun removeFromWatchlist(movie: Movie) {
        unsupported
    }

    override suspend fun addSuggestions(movies: Collection<Movie>) {
        unsupported
    }

    override suspend fun removeSuggestion(movie: Movie) {
        unsupported
    }
}

object Test {

    object EmailAddress {

        val Valid = EmailAddress("some@email.it")
        val Empty = EmailAddress("")
        val WrongFormat = EmailAddress("invalid")
    }

    object Password {

        val Valid = Password("aValidPsw")
        val Empty = Password("")
        val Short = Password("hi")
    }

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
            poster = ImageUrl("/8sV6nWuKczuXRt0C6EWoXqJAj6G.jpg"),
            backdrop = null,
            actors = setOf(DenzelWashington, RussellCrowe, ChiwetelEjiofor),
            genres = setOf(Drama, Crime),
            year = 2007u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val Blow = Movie(
            id = TmdbId(4133),
            name = Name("Blow"),
            poster = ImageUrl("/ii4sylRdQnLFPMCLhaER7vb0J6N.jpg"),
            backdrop = null,
            actors = setOf(JohnnyDepp, PenelopeCruz, EthanSuplee),
            genres = setOf(Crime, Drama),
            year = 2001u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val DejaVu = Movie(
            id = TmdbId(7551),
            name = Name("Déjà Vu"),
            poster = ImageUrl("/hL8W0qgoPKw7xQy7LMir2numqsP.jpg"),
            backdrop = null,
            actors = setOf(DenzelWashington, PaulaPatton, ValKilmer),
            genres = setOf(Action, Thriller, ScienceFiction),
            year = 2006u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val DjangoUnchained = Movie(
            id = TmdbId(18921),
            name = Name("Django Unchained"),
            poster = ImageUrl("/7oWY8VDWW7thTzWh3OKYRkWUlD5.jpg"),
            backdrop = null,
            actors = setOf(JamieFoxx, ChristophWaltz, LeonardoDiCaprio),
            genres = setOf(Drama, Western),
            year = 2012u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val Fury = Movie(
            id = TmdbId(228150),
            name = Name("Fury"),
            poster = ImageUrl("/pfte7wdMobMF4CVHuOxyu6oqeeA.jpg"),
            backdrop = null,
            actors = setOf(BradPitt, ShiaLaBeouf, LoganLerman),
            genres = setOf(Genre.War, Drama, Action),
            year = 2014u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val Inception = Movie(
            id = TmdbId(27205),
            name = Name("Inception"),
            poster = ImageUrl("/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg"),
            backdrop = null,
            actors = setOf(LeonardoDiCaprio, JosephGordonLevitt, EllenPage, TomHardy, KenWatanabe),
            genres = setOf(Action, ScienceFiction, Adventure),
            year = 2010u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val JohnWick = Movie(
            id = TmdbId(245891),
            name = Name("John Wick"),
            poster = ImageUrl("/h3VxEVUOoBZmo79O8RqKvyGiqmE.jpg"),
            backdrop = null,
            actors = setOf(KeanuReeves, MichaelNyqvist, AlfieAllen),
            genres = setOf(Action, Thriller),
            year = 2014u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val PulpFiction = Movie(
            id = TmdbId(680),
            name = Name("Pulp Fiction"),
            poster = ImageUrl("/plnlrtBUULT0rh3Xsjmpubiso3L.jpg"),
            backdrop = null,
            actors = setOf(JohnTravolta, SamuelLJackson, UmaThurman),
            genres = setOf(Crime, Thriller),
            year = 1994u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val SinCity = Movie(
            id = TmdbId(187),
            name = Name("Sin City"),
            poster = ImageUrl("/1Br0CXgpDIgF0ue7HVhO08bn7kn.jpg"),
            backdrop = null,
            actors = setOf(BruceWillis, JessicaAlba, CliveOwen),
            genres = setOf(Action, Thriller, Crime),
            year = 2005u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val TheBookOfEli = Movie(
            id = TmdbId(20504),
            name = Name("The Book of Eli"),
            poster = ImageUrl("/1H1y9ZiqNFaLgQiRDDZLA55PviW.jpg"),
            backdrop = null,
            actors = setOf(DenzelWashington, GaryOldman, MilaKunis),
            genres = setOf(Action, Thriller, ScienceFiction),
            year = 2010u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val TheEqualizer = Movie(
            id = TmdbId(156022),
            name = Name("The Equalizer"),
            poster = ImageUrl("/9u4yW7yPA0BQ2pv9XwiNzItwvp8.jpg"),
            backdrop = null,
            actors = setOf(DenzelWashington),
            genres = setOf(Action, Crime, Thriller),
            year = 2014u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val TheGreatDebaters = Movie(
            id = TmdbId(14047),
            name = Name("The Great Debaters"),
            poster = ImageUrl("/jxsWIZzjpaRNd0Ni4v3iISk3SRr.jpg"),
            backdrop = null,
            actors = setOf(DenzelWashington, NateParker, ForestWhitaker),
            genres = setOf(Drama),
            year = 2007u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val TheHatefulEight = Movie(
            id = TmdbId(273248),
            name = Name("The Hateful Eight"),
            poster = ImageUrl("/nZeKw2oDiODgnht9OrohB2jBhjq.jpg"),
            backdrop = null,
            actors = setOf(SamuelLJackson, KurtRussell, JenniferJasonLeigh),
            genres = setOf(Crime, Drama, Mystery, Western),
            year = 2015u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val War = Movie(
            id = TmdbId(0),
            name = Name("War"),
            poster = null,
            backdrop = null,
            actors = setOf(HrithikRoshan, TigerShroff, VaaniKapoor),
            genres = setOf(Action, Thriller),
            year = 2019u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )
        val Willard = Movie(
            id = TmdbId(10929),
            name = Name("Willard"),
            poster = ImageUrl("/6FMNo5aBX7tAiNigmFGeopPBBqh.jpg"),
            backdrop = null,
            actors = setOf(CrispinGlover, LeeErmey, LauraHarring),
            genres = setOf(Horror),
            year = 2003u,
            rating = CommunityRating(0.0, 0u),
            overview = "",
            videos = emptyList()
        )

        private fun ImageUrl(path: String) = TmdbImageUrl("https://image.tmdb.org/t/p", path)
    }
}
