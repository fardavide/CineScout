import assert4k.*
import database.stats.MovieDetailsWithRating
import database.stats.StatType
import database.stats.StatType.*
import domain.Test.Actor.DenzelWashington
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
import stats.local.LocalStatSourceImpl
import kotlin.test.Test

internal class LocalStatSourceImplTest {

    private val actors = mutableListOf<Pair<TmdbId, Name>>()
    private val genres = mutableListOf<Pair<TmdbId, Name>>()
    private val movies = mutableListOf<Triple<TmdbId, Name, UInt>>()
    private val movieActors = mutableListOf<Pair<IntId, IntId>>()
    private val movieGenres = mutableListOf<Pair<IntId, IntId>>()
    private val stats = mutableListOf<Triple<IntId, StatType, Int>>()
    private val years = mutableSetOf<FiveYearRange>()

    private val source = LocalStatSourceImpl(
        actors = mockk {
            var lastIndex = -1

            every { insert(TmdbId(any()), Name(any())) } answers {
                val idArg = TmdbId(firstArg())
                val nameArg = Name(secondArg())
                val index = actors.indexOf { (tmdbId, name) -> tmdbId == idArg && name == nameArg }
                actors.insert(index, idArg to nameArg)
                lastIndex = index ?: actors.lastIndex
            }

            every { lastInsertRowId().executeAsOne() } answers { lastIndex.toLong() }
        },
        genres = mockk {
            var lastIndex = -1

            every { insert(TmdbId(any()), Name(any())) } answers {
                val idArg = TmdbId(firstArg())
                val nameArg = Name(secondArg())
                val index = genres.indexOf { (tmdbId, name) -> tmdbId == idArg && name == nameArg }
                genres.insert(index, idArg to nameArg)
                lastIndex = index ?: genres.lastIndex
            }

            every { lastInsertRowId().executeAsOne() } answers { lastIndex.toLong() }
        },
        movies = mockk {
            var lastIndex = -1

            every { insert(tmdbId = TmdbId(any()), title = Name(any()), year = any<Int>().toUInt()) } answers {
                val idArg = TmdbId(firstArg())
                val index = movies.indexOf { (tmdbId, _, _) -> tmdbId == idArg }
                movies.insert(index, Triple(idArg, Name(secondArg()), thirdArg<Int>().toUInt()))
                lastIndex = index ?: movies.lastIndex
            }

            every { lastInsertRowId().executeAsOne() } answers { lastIndex.toLong() }

            every { selectAllRated().executeAsList() } answers {
                movies.flatMapIndexed { index: Int, triple: Triple<TmdbId, Name, UInt> ->
                    val movieId = IntId(index)
                    val movieTmdbId = triple.first
                    val movieTitle = triple.second
                    val movieYear = triple.third

                    val actors =
                        movieActors.filter { (movieId, _) -> movieId == movieId }.map { it.second }.map { actors[it.i] }
                    val genres =
                        movieGenres.filter { (movieId, _) -> movieId == movieId }.map { it.second }.map { genres[it.i] }

                    actors.flatMap { actor ->
                        genres.map { genre ->
                            MovieDetailsWithRating(
                                id = movieId,
                                tmdbId = movieTmdbId,
                                title = movieTitle,
                                year = movieYear,
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
            var lastIndex = -1

            every { insert(IntId(any()), IntId(any())) } answers {
                val movieIdArg = IntId(firstArg())
                val actorIdArg = IntId(secondArg())
                val index = movieActors.indexOf { (movieId, actorId) -> movieId == movieIdArg && actorId == actorIdArg }
                movieActors.insert(index, movieIdArg to actorIdArg)
                lastIndex = index ?: movieActors.lastIndex
            }
        },
        movieGenres = mockk {
            var lastIndex = -1

            every { insert(IntId(any()), IntId(any())) } answers {
                val movieIdArg = IntId(firstArg())
                val genreIdArg = IntId(secondArg())
                val index = movieGenres.indexOf { (movieId, genreId) -> movieId == movieIdArg && genreId == genreIdArg }
                movieGenres.insert(index, movieIdArg to genreIdArg)
                lastIndex = index ?: movieGenres.lastIndex
            }
        },
        stats = mockk {
            var lastIndex = -1

            every { insert(statId = IntId(any()), type = any(), rating = any()) } answers {
                val idArg = IntId(firstArg())
                val typeArg = secondArg<StatType>()
                val ratingArg = thirdArg<Int>()

                val index = stats.indexOf { (statId, type, _ ) -> statId == idArg && type == secondArg() }
                val prev = index?.let { stats[it].third } ?: 0
                val new =
                    if (typeArg == MOVIE) ratingArg
                    else prev + ratingArg
                stats.insert(index, Triple(idArg, typeArg, new))
                lastIndex = index ?: stats.lastIndex
            }

            every { selectActorRating(IntId(any())).executeAsOne() } answers {
                runCatching {
                    stats.first { (statId, type, _) -> statId == IntId(firstArg()) && type == ACTOR }.third
                }.getOrNull() ?: 0
            }

            every { selectGenreRating(IntId(any())).executeAsOne() } answers {
                runCatching {
                    stats.first { (statId, type, _) -> statId == IntId(firstArg()) && type == GENRE }.third
                }.getOrNull() ?: 0
            }

            every { selectMovieRating(IntId(any())).executeAsOne() } answers {
                runCatching {
                    stats.first { (statId, type, _) -> statId == IntId(firstArg()) && type == MOVIE }.third
                }.getOrNull() ?: 0
            }

            every { selectYearRating(IntId(any())).executeAsOne() } answers {
                runCatching {
                    stats.first { (statId, type, _) -> statId == IntId(firstArg()) && type == FIVE_YEAR_RANGE }.third
                }.getOrNull() ?: 0
            }
        },
        years = mockk {
            var lastIndex = -1

            every { insert(any()) } answers {
                years += FiveYearRange(firstArg())
            }

            every { lastInsertRowId().executeAsOne() } answers { years.size - 1.toLong() }
        },
    )

    private fun <T> Collection<T>.indexOf(find: (T) -> Boolean) = indexOfFirst(find).takeIf { it >= 0 }
    private fun <T> MutableList<T>.insert(index: Int?, element: T) {
        if (index != null) this[index] = element
        else this += element
    }

    @Test
    fun `ratedMovies returns right result after rate one movie positive`() = runBlockingTest {
        source.rate(Inception, Positive)

        assert that source.ratedMovies().positives().first() *{
            +name equals Inception.name
            +actors `equals no order` Inception.actors
            +genres `equals no order` Inception.genres
            +year equals Inception.year
        }
    }

    @Test
    fun `ratedMovies returns right result after rate more movies positive`() = runBlockingTest {
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
    fun `ratedMovies returns right result after rate more movies positive and negative`() = runBlockingTest {
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
    fun `ratedMovies returns right result if rate negative before positive`() = runBlockingTest {
        source.rate(Inception, Positive)
        source.rate(TheBookOfEli, Negative)

        assert that source.ratedMovies() * {
            +positives().first().name equals Inception.name
            +negatives().first().name equals TheBookOfEli.name
        }

        source.rate(Inception, Negative)
        source.rate(TheBookOfEli, Positive)

        assert that source.ratedMovies() * {
            +positives().first().name equals TheBookOfEli.name
            +negatives().first().name equals Inception.name
        }
    }

    @Test
    fun `actor rating is decreased when rated negative`() = runBlockingTest {
        source.run {
            rate(AmericanGangster, Positive)
            rate(DejaVu, Positive)
            rate(TheBookOfEli, Positive)
        }

        val id1 = IntId(actors.indexOf { it.first == DenzelWashington.id }!!)
        val rating1 = stats.first { (id, type, _) -> id == id1 && type == ACTOR }.third
        assert that rating1 equals 3

        source.rate(TheGreatDebaters, Negative)

        val id2 = IntId(actors.indexOf { it.first == DenzelWashington.id }!!)
        val rating2 = stats.first { (id, type, _) -> id == id2 && type == ACTOR }.third
        assert that rating2 equals 2
    }

    @Test
    fun `genre rating is decreased when rated negative`() = runBlockingTest {
        source.run {
            rate(AmericanGangster, Positive)
            rate(Blow, Positive)
            rate(DjangoUnchained, Positive)
        }

        val id1 = IntId(genres.indexOf { it.first == Drama.id }!!)
        val rating1 = stats.first { (id, type, _) -> id == id1 && type == GENRE }.third
        assert that rating1 equals 3

        source.rate(Fury, Negative)

        val id2 = IntId(genres.indexOf { it.first == Drama.id }!!)
        val rating2 = stats.first { (id, type, _) -> id == id2 && type == GENRE }.third
        assert that rating2 equals 2
    }

    @Test
    fun `year rating is decreased when rated negative`() = runBlockingTest {
        source.run {
            rate(AmericanGangster, Positive)
            rate(DejaVu, Positive)
            rate(SinCity, Positive)
        }

        val id1 = IntId(years.indexOf { it == FiveYearRange(2010u) }!!)
        val rating1 = stats.first { (id, type, _) -> id == id1 && type == FIVE_YEAR_RANGE }.third
        assert that rating1 equals 3

        source.rate(TheGreatDebaters, Negative)

        val id2 = IntId(years.indexOf { it == FiveYearRange(2010u) }!!)
        val rating2 = stats.first { (id, type, _) -> id == id2 && type == FIVE_YEAR_RANGE }.third
        assert that rating2 equals 2
    }
}
