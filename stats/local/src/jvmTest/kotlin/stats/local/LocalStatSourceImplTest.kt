package stats.local

import assert4k.*
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import database.Database
import database.actorAdapter
import database.databaseModule
import database.genreAdapter
import database.movieActorAdapter
import database.movieAdapter
import database.movieGenreAdapter
import database.movieVideoAdapter
import database.profileAdapter
import database.statAdapter
import database.tmdbCredentialAdapter
import database.videoAdapter
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
import entities.model.FiveYearRange
import entities.model.UserRating
import entities.model.UserRating.Negative
import entities.model.UserRating.Neutral
import entities.model.UserRating.Positive
import entities.stats.negatives
import entities.stats.positives
import io.mockk.isMockKMock
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.core.context.startKoin
import org.koin.dsl.module
import stats.LocalStatSource
import stats.local.double.mockLocalStatSource
import util.test.CoroutinesTest
import kotlin.test.*
import kotlin.time.seconds

@RunWith(Parameterized::class)
internal class LocalStatSourceImplTest(
    private val getSource: () -> LocalStatSourceImpl
) : CoroutinesTest {

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
                        movie_videoAdapter = get(movieVideoAdapter),
                        profileAdapter = get(profileAdapter),
                        statAdapter = get(statAdapter),
                        tmdbCredentialAdapter = get(tmdbCredentialAdapter),
                        videoAdapter = get(videoAdapter),
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
            rateActors(setOf(DenzelWashington).map { insertActor(it) }, Positive)
            assert that actorRating(DenzelWashington.id) equals 1

            rateActors(setOf(DenzelWashington).map { insertActor(it) }, Positive)
            assert that actorRating(DenzelWashington.id) equals 2

            rateActors(setOf(DenzelWashington).map { insertActor(it) }, Negative)
            assert that actorRating(DenzelWashington.id) equals 1
        }
    }

    @Test
    fun `genreRating returns right result`() = runBlockingTest {
        getSource().run {
            rateGenres(setOf(Crime).map { insertGenre(it) }, Positive)
            assert that genreRating(Crime.id) equals 1

            rateGenres(setOf(Crime).map { insertGenre(it) }, Positive)
            assert that genreRating(Crime.id) equals 2

            rateGenres(setOf(Crime).map { insertGenre(it) }, Negative)
            assert that genreRating(Crime.id) equals 1
        }
    }

    @Test
    fun `yearRating returns right result`() = runBlockingTest {
        val year = 2010u
        val yearRange = FiveYearRange(forYear = year)

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
    fun `rating works correctly`() = coroutinesTest {
        withTimeoutOrNull(5.seconds) {
            val source = getSource()
            // Ignore mock for this test, cannot mock listeners for db
            if (isMockKMock(source, spy = true)) return@withTimeoutOrNull

            val result = mutableListOf<UserRating>()
            val job = launch(Unconfined) {
                source.rating(Fury).toList(result)
            }

            while (result.isEmpty()) {
                delay(1)
            }
            assert that result equals listOf(Neutral)

            source.rate(Fury, Positive)
            while (result.size == 1) {
                delay(1)
            }
            assert that result equals listOf(Neutral, Positive)

            source.rate(Fury, Negative)
            while (result.size == 2) {
                delay(1)
            }
            assert that result equals listOf(Neutral, Positive, Negative)

            job.cancel()
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
    fun `watchlist works correctly`() = coroutinesTest {
        val source = getSource()
        assert that source.watchlist() equals emptyList()

        source.addToWatchlist(Fury)
        assert that source.watchlist().first().id equals Fury.id

        source.removeFromWatchlist(Fury)
        assert that source.watchlist() equals emptyList()
    }

    @Test
    // TODO Flow is not emitting, inspect why!
    @Ignore("Flow is not emitting, inspect why!")
    fun `isInWatchlist works correctly`() = coroutinesTest {
        val source = getSource()
        // Ignore mock for this test, cannot mock listeners for db
        if (isMockKMock(source, spy = true)) return@coroutinesTest

        val result = mutableListOf<Boolean>()
        val job = launch(Unconfined) {
            source.isInWatchlist(Fury).toList(result)
        }

        while (result.isEmpty()) { delay(1) }
        assert that result equals listOf(false)

        source.addToWatchlist(Fury)
        while (result.isEmpty()) { delay(1) }
        assert that result equals listOf(false, true)

        source.removeFromWatchlist(Fury)
        while (result.size == 2) { delay(1) }
        assert that result equals listOf(false, true, false)

        job.cancel()
    }
}
