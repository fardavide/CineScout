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
import database.watchlistAdapter
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
import entities.Rating.Negative
import entities.Rating.Positive
import entities.stats.negatives
import entities.stats.positives
import kotlinx.coroutines.test.runBlockingTest
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.core.context.startKoin
import org.koin.dsl.module
import stats.LocalStatSource
import stats.local.LocalStatSourceImpl
import stats.local.double.mockLocalStatSource
import stats.local.localStatsModule
import kotlin.test.*

@RunWith(Parameterized::class)
internal class LocalStatSourceImplTest(
    private val getSource: () -> LocalStatSourceImpl
) {

    companion object {

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
                        watchlistAdapter = get(watchlistAdapter),
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
            arrayOf( { mockLocalStatSource() } ),
            arrayOf( { realSource } )
        )
    }

    @Test
    fun `actorRating returns right result`() = runBlockingTest {
        getSource().run {
            rateActors(setOf(DenzelWashington).map(::insertActor), Positive)
            assert that actorRating(DenzelWashington.id) equals 1

            rateActors(setOf(DenzelWashington).map(::insertActor), Positive)
            assert that actorRating(DenzelWashington.id) equals 2

            rateActors(setOf(DenzelWashington).map(::insertActor), Negative)
            assert that actorRating(DenzelWashington.id) equals 1
        }
    }

    @Test
    fun `genreRating returns right result`() = runBlockingTest {
        getSource().run {
            rateGenres(setOf(Crime).map(::insertGenre), Positive)
            assert that genreRating(Crime.id) equals 1

            rateGenres(setOf(Crime).map(::insertGenre), Positive)
            assert that genreRating(Crime.id) equals 2

            rateGenres(setOf(Crime).map(::insertGenre), Negative)
            assert that genreRating(Crime.id) equals 1
        }
    }

    @Test
    fun `yearRating returns right result`() = runBlockingTest {
        val year = 2010u
        val yearRange = FiveYearRange(year)

        getSource().run {

            rateYear(insertYear(year), Positive)
            assert that yearRating(yearRange) equals 1

            rateYear(insertYear(year), Positive)
            assert that yearRating(yearRange) equals 2

            rateYear(insertYear(year), Negative)
            assert that yearRating(yearRange) equals 1
        }
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

    @Test
    fun `watchlist works correctly`() = runBlockingTest { val source = getSource()
        assert that source.watchlist() equals emptyList()

        source.addToWatchlist(TheBookOfEli)

        assert that source.watchlist() equals listOf(TheBookOfEli)
    }
}
