import assert4k.*
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import database.Database
import database.actorAdapter
import database.databaseModule
import database.genreAdapter
import database.movieActorAdapter
import database.movieAdapter
import database.movieGenreAdapter
import database.statAdapter
import database.stats.Movie
import database.stats.MovieDetailsWithRating
import database.stats.StatType
import database.stats.StatType.ACTOR
import database.stats.StatType.FIVE_YEAR_RANGE
import database.stats.StatType.GENRE
import database.stats.StatType.MOVIE
import database.yearRangeAdapter
import domain.Test.Actor.DenzelWashington
import domain.Test.Genre.Crime
import domain.Test.Genre.Drama
import domain.Test.Movie.AmericanGangster
import domain.Test.Movie.Blow
import domain.Test.Movie.DejaVu
import domain.Test.Movie.DjangoUnchained
import domain.Test.Movie.Fury
import domain.Test.Movie.Inception
import domain.Test.Movie.SinCity
import domain.Test.Movie.TheBookOfEli
import domain.Test.Movie.TheGreatDebaters
import domain.Test.Movie.Willard
import entities.FiveYearRange
import entities.IntId
import entities.Name
import entities.Rating.Negative
import entities.Rating.Positive
import entities.TmdbId
import entities.stats.negatives
import entities.stats.positives
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.core.context.startKoin
import org.koin.dsl.module
import stats.LocalStatSource
import stats.local.LocalStatSourceImpl
import stats.local.localStatsModule
import kotlin.test.*

@RunWith(Parameterized::class)
internal class LocalStatSourceImplTest(
    private val getSource: () -> LocalStatSourceImpl
) {

    companion object {

        private val mockSource: LocalStatSource get() {
            val actors = mutableListOf<Pair<TmdbId, Name>>()
            val genres = mutableListOf<Pair<TmdbId, Name>>()
            val movies = mutableListOf<Movie>()
//            val movies = mutableListOf<Triple<TmdbId, Name, UInt>>()
            val movieActors = mutableListOf<Pair<IntId, IntId>>()
            val movieGenres = mutableListOf<Pair<IntId, IntId>>()
            val stats = mutableListOf<Triple<IntId, StatType, Int>>()
            val years = mutableSetOf<FiveYearRange>()

            fun <T> Collection<T>.indexOf(find: (T) -> Boolean) = indexOfFirst(find).takeIf { it >= 0 }
            fun <T> MutableList<T>.insert(index: Int?, element: T) {
                if (index != null) this[index] = element
                else this += element
            }

            return LocalStatSourceImpl(
                actors = mockk {

                    every { insert(TmdbId(any()), Name(any())) } answers {
                        val idArg = TmdbId(firstArg())
                        val nameArg = Name(secondArg())
                        val index = actors.indexOf { (tmdbId, name) -> tmdbId == idArg && name == nameArg }
                        actors.insert(index, idArg to nameArg)
                    }

                    every { selectIdByTmdbId(TmdbId(any())) } answers {
                        val tmdbIdArg = TmdbId(firstArg())
                        mockk {
                            every { executeAsOne() } answers {
                                IntId(actors.indexOf { it.first == tmdbIdArg }!!)
                            }
                        }
                    }
                },
                genres = mockk {

                    every { insert(TmdbId(any()), Name(any())) } answers {
                        val idArg = TmdbId(firstArg())
                        val nameArg = Name(secondArg())
                        val index = genres.indexOf { (tmdbId, name) -> tmdbId == idArg && name == nameArg }
                        genres.insert(index, idArg to nameArg)
                    }

                    every { selectIdByTmdbId(TmdbId(any())) } answers {
                        val tmdbIdArg = TmdbId(firstArg())
                        mockk {
                            every { executeAsOne() } answers {
                                IntId(genres.indexOf { it.first == tmdbIdArg }!!)
                            }
                        }
                    }
                },
                movies = mockk {

                    every {
                        insert(
                            tmdbId = TmdbId(any()),
                            title = Name(any()),
                            year = any<Int>().toUInt(),
                            posterBaseUrl = any(),
                            posterPath = any()
                        )
                    } answers {
                        val idArg = firstArg<Int>()
                        val tmdbIdArg = TmdbId(idArg)
                        val index = movies.indexOf { it.tmdbId == tmdbIdArg }
                        val movie =
                            Movie(IntId(idArg), tmdbIdArg, Name(secondArg()), thirdArg<Int>().toUInt(), arg(3), arg(4))
                        movies.insert(index, movie)
                    }

                    every { selectIdByTmdbId(TmdbId(any())) } answers {
                        val tmdbIdArg = TmdbId(firstArg())
                        mockk {
                            every { executeAsOne() } answers {
                                IntId(movies.indexOf { it.tmdbId == tmdbIdArg }!!)
                            }
                        }
                    }

                    every { selectAllRated().executeAsList() } answers {
                        movies.flatMapIndexed { index: Int, movie: Movie ->
                            val movieId = IntId(index)

                            val moviesActors =
                                movieActors.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                                    .map { actors[it.i] }
                            val moviesGenres =
                                movieGenres.filter { (movieId, _) -> movieId == movieId }.map { it.second }
                                    .map { genres[it.i] }

                            moviesActors.flatMap { actor ->
                                moviesGenres.map { genre ->
                                    MovieDetailsWithRating(
                                        id = movieId,
                                        tmdbId = movie.tmdbId,
                                        title = movie.title,
                                        year = movie.year,
                                        posterBaseUrl = movie.posterBaseUrl,
                                        posterPath = movie.posterPath,
                                        actorTmdbId = actor.first,
                                        actorName = actor.second,
                                        genreTmdbId = genre.first,
                                        genreName = genre.second,
                                        rating = stats.find { (statId, type, _) -> statId == movieId && type == MOVIE }?.third
                                            ?: 0
                                    )
                                }
                            }
                        }
                    }
                },
                movieActors = mockk {

                    every { insert(IntId(any()), IntId(any())) } answers {
                        val movieIdArg = IntId(firstArg())
                        val actorIdArg = IntId(secondArg())
                        val index =
                            movieActors.indexOf { (movieId, actorId) -> movieId == movieIdArg && actorId == actorIdArg }
                        movieActors.insert(index, movieIdArg to actorIdArg)
                    }
                },
                movieGenres = mockk {

                    every { insert(IntId(any()), IntId(any())) } answers {
                        val movieIdArg = IntId(firstArg())
                        val genreIdArg = IntId(secondArg())
                        val index =
                            movieGenres.indexOf { (movieId, genreId) -> movieId == movieIdArg && genreId == genreIdArg }
                        movieGenres.insert(index, movieIdArg to genreIdArg)
                    }
                },
                stats = mockk {

                    every { insert(statId = IntId(any()), type = any(), rating = any()) } answers {
                        val idArg = IntId(firstArg())
                        val typeArg = secondArg<StatType>()
                        val ratingArg = thirdArg<Int>()

                        val index = stats.indexOf { (statId, type, _) -> statId == idArg && type == secondArg() }
                        stats.insert(index, Triple(idArg, typeArg, ratingArg))
                    }

                    // selectActorRating
                    every { selectActorRating(IntId(any())) } answers {
                        val intId = IntId(firstArg())
                        mockk {
                            every { executeAsOneOrNull() } answers {
                                stats.find { (statId, type, _) -> statId == intId && type == ACTOR }?.third
                            }
                        }
                    }

                    // selectActorRatingByTmdbId
                    every { selectActorRatingByTmdbId(TmdbId(any())) } answers {
                        val tmdbIdArg = TmdbId(firstArg())
                        mockk {
                            every { executeAsOneOrNull() } answers {
                                val id = actors.indexOf { it.first == tmdbIdArg }?.let(::IntId)
                                stats.find { (statId, type, _) -> statId == id && type == ACTOR }?.third
                            }
                        }
                    }

                    // selectGenreRating
                    every { selectGenreRating(IntId(any())) } answers {
                        val intId = IntId(firstArg())
                        mockk {
                            every { executeAsOneOrNull() } answers {
                                stats.find { (statId, type, _) -> statId == intId && type == GENRE }?.third
                            }
                        }
                    }

                    // selectGenreRatingByTmdbId
                    every { selectGenreRatingByTmdbId(TmdbId(any())) } answers {
                        val tmdbIdArg = TmdbId(firstArg())
                        mockk {
                            every { executeAsOneOrNull() } answers {
                                val id = genres.indexOf { it.first == tmdbIdArg }?.let(::IntId)
                                stats.find { (statId, type, _) -> statId == id && type == GENRE }?.third
                            }
                        }
                    }

                    // selectMovieRating
                    every { selectMovieRating(IntId(any())) } answers {
                        val intId = IntId(firstArg())
                        mockk {
                            every { executeAsOneOrNull() } answers {
                                stats.find { (statId, type, _) -> statId == intId && type == MOVIE }?.third
                            }
                        }
                    }

                    // selectMovieRatingByTmdbId
                    every { selectMovieRatingByTmdbId(TmdbId(any())) } answers {
                        val tmdbId = TmdbId(firstArg())
                        mockk {
                            every { executeAsOneOrNull() } answers {
                                val id = movies.indexOf { it.tmdbId == tmdbId }?.let(::IntId)
                                stats.find { (statId, type, _) -> statId == id && type == MOVIE }?.third
                            }
                        }
                    }

                    // selectYearRating
                    every { selectYearRating(IntId(any())) } answers {
                        val intId = IntId(firstArg())
                        mockk {
                            every { executeAsOneOrNull() } answers {
                                stats.find { (statId, type, _) -> statId == intId && type == FIVE_YEAR_RANGE }?.third
                            }
                        }
                    }

                    every { selectYearRatingById(any<Int>().toUInt()) } answers {
                        mockk {
                            val intId = IntId(firstArg())
                            every { executeAsOneOrNull() } answers {
                                stats.find { (statId, type) -> statId == intId && type == FIVE_YEAR_RANGE }?.third
                            }
                        }
                    }
                },
                years = mockk {

                    every { insert(any()) } answers {
                        years += FiveYearRange(firstArg())
                    }

                    every { lastInsertRowId().executeAsOne() } answers { years.size - 1.toLong() }
                },
            )
        }

        private val koin = startKoin {
            modules(localStatsModule)
        }.koin
        private val realSource get(): LocalStatSource {
            koin.unloadModules(listOf(databaseModule))
            val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
            koin.loadModules(databaseModule + module(override = true) {
                single {
                    Database(
                        driver = driver,
                        actorAdapter = get(actorAdapter),
                        genreAdapter = get(genreAdapter),
                        movieAdapter = get(movieAdapter),
                        movie_actorAdapter = get(movieActorAdapter),
                        movie_genreAdapter = get(movieGenreAdapter),
                        statAdapter = get(statAdapter),
                        yearRangeAdapter = get(yearRangeAdapter),
                    ).also {
                        Database.Schema.create(driver)
                    }
                }
            })
            return koin.get()
        }

        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf( { mockSource } ),
            arrayOf( { realSource } )
        )
    }

    @Test
    fun `actorRating returns right result`() = runBlockingTest { val source = getSource()
        source.rateActors(setOf(DenzelWashington), Positive)
        assert that source.actorRating(DenzelWashington.id) equals 1

        source.rateActors(setOf(DenzelWashington), Positive)
        assert that source.actorRating(DenzelWashington.id) equals 2

        source.rateActors(setOf(DenzelWashington), Negative)
        assert that source.actorRating(DenzelWashington.id) equals 1
    }

    @Test
    fun `genreRating returns right result`() = runBlockingTest { val source = getSource()
        source.rateGenres(setOf(Crime), Positive)
        assert that source.genreRating(Crime.id) equals 1

        source.rateGenres(setOf(Crime), Positive)
        assert that source.genreRating(Crime.id) equals 2

        source.rateGenres(setOf(Crime), Negative)
        assert that source.genreRating(Crime.id) equals 1
    }

    @Test
    fun `yearRating returns right result`() = runBlockingTest { val source = getSource()
        val yearRange = FiveYearRange(2010u)
        source.rateYear(yearRange, Positive)
        assert that source.yearRating(yearRange) equals 1

        source.rateYear(yearRange, Positive)
        assert that source.yearRating(yearRange) equals 2

        source.rateYear(yearRange, Negative)
        assert that source.yearRating(yearRange) equals 1
    }

    @Test
    fun `ratedMovies returns right result after rate one movie positive`() = runBlockingTest { val source = getSource()
        source.rate(Inception, Positive)

        assert that source.ratedMovies().positives().first() *{
            +name equals Inception.name
            +actors `equals no order` Inception.actors
            +genres `equals no order` Inception.genres
            +year equals Inception.year
        }
    }

    @Test
    fun `ratedMovies returns right result after rate more movies positive`() = runBlockingTest { val source = getSource()
        source.rate(Inception, Positive)
        source.rate(TheBookOfEli, Positive)

        val result = source.ratedMovies()
        assert that result.size equals 2 { result.joinToString { "${it.first.name} - ${it.second}" } }
        val (one, two) = source.ratedMovies().toList()
        assert that one() * {
            +underlying.first.name equals Inception.name
            +underlying.second equals Positive
        }
        assert that two() * {
            +underlying.first.name equals TheBookOfEli.name
            +underlying.second equals Positive
        }
    }

    @Test
    fun `ratedMovies returns right result after rate more movies positive and negative`() = runBlockingTest { val source = getSource()
        source.run {
            rate(Inception, Positive)
            rate(TheBookOfEli, Positive)
            rate(Willard, Negative)
        }

        val result = source.ratedMovies()
        assert that result.size equals 3 { result.joinToString { "${it.first.name} - ${it.second}" } }
        val (one, two, three) = source.ratedMovies().toList()
        assert that one() * {
            +underlying.first.name equals Inception.name
            +underlying.second equals Positive
        }
        assert that two() * {
            +underlying.first.name equals TheBookOfEli.name
            +underlying.second equals Positive
        }
        assert that three() * {
            +underlying.first.name equals Willard.name
            +underlying.second equals Negative
        }
    }

    @Test
    fun `ratedMovies returns right result if rate negative before positive`() = runBlockingTest { val source = getSource()
        source.rate(Inception, Positive)
        source.rate(TheBookOfEli, Negative)

        assert that source.ratedMovies() * {
            +positives().first().name equals Inception.name
            +negatives().first().name equals TheBookOfEli.name
        }

        source.rate(Inception, Negative)
        source.rate(TheBookOfEli, Positive)

        val ratedMovies = source.ratedMovies()
        assert that ratedMovies * {
            +positives().first().name equals TheBookOfEli.name
            +negatives().first().name equals Inception.name
        }
    }

    @Test
    fun `actor rating is decreased when rated negative`() = runBlockingTest { val source = getSource()
        source.run {
            rate(AmericanGangster, Positive)
            rate(DejaVu, Positive)
            rate(TheBookOfEli, Positive)
        }

        val rating1 = source.actorRating(DenzelWashington.id)
        assert that rating1 equals 3

        source.rate(TheGreatDebaters, Negative)

        val rating2 = source.actorRating(DenzelWashington.id)
        assert that rating2 equals 2
    }

    @Test
    fun `genre rating is decreased when rated negative`() = runBlockingTest { val source = getSource()
        source.run {
            rate(AmericanGangster, Positive)
            rate(Blow, Positive)
            rate(DjangoUnchained, Positive)
        }

        val rating1 = source.genreRating(Drama.id)
        assert that rating1 equals 3

        source.rate(Fury, Negative)

        val rating2 = source.genreRating(Drama.id)
        assert that rating2 equals 2
    }

    @Test
    fun `year rating is decreased when rated negative`() = runBlockingTest { val source = getSource()
        source.run {
            rate(AmericanGangster, Positive)
            rate(DejaVu, Positive)
            rate(SinCity, Positive)
        }

        val rating1 = source.yearRating(FiveYearRange(2010u))
        assert that rating1 equals 3

        source.rate(TheGreatDebaters, Negative)

        val rating2 = source.yearRating(FiveYearRange(2010u))
        assert that rating2 equals 2
    }
}
