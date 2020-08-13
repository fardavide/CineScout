import Test.Movie.Inception
import assert4k.*
import entities.IntId
import entities.Name
import entities.Rating
import entities.TmdbId
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import stats.local.LocalStatSourceImpl
import stats.local.StatType
import stats.local.StatType.ACTOR
import stats.local.StatType.FIVE_YEAR_RANGE
import stats.local.StatType.GENRE
import stats.local.StatType.MOVIE
import statslocal.MovieDetailsWithRating
import kotlin.test.Test

internal class LocalStatSourceImplTest {

    private val actors = mutableListOf<Pair<TmdbId, Name>>()
    private val genres = mutableListOf<Pair<TmdbId, Name>>()
    private val movies = mutableListOf<Triple<TmdbId, Name, UInt>>()
    private val movieActors = mutableListOf<Pair<IntId, IntId>>()
    private val movieGenres = mutableListOf<Pair<IntId, IntId>>()
    private val stats = mutableListOf<Triple<IntId, StatType, Int>>()
    private val years = mutableSetOf<UInt>()

    private val source = LocalStatSourceImpl(
        actors = mockk {
            var lastIndex = -1

            every { insert(TmdbId(any()), Name(any())) } answers {
                val idArg = TmdbId(firstArg())
                val index = actors.indexOf { (tmdbId, name) -> tmdbId == idArg && name == secondArg() }
                actors.insert(index, idArg to Name(secondArg()))
                lastIndex = index ?: actors.lastIndex
            }

            every { lastInsertRowId().executeAsOne() } answers { lastIndex.toLong() }
        },
        genres = mockk {
            var lastIndex = -1

            every { insert(TmdbId(any()), Name(any())) } answers {
                val idArg = TmdbId(firstArg())
                val index = genres.indexOf { (tmdbId, name) -> tmdbId == idArg && name == secondArg() }
                genres.insert(index, idArg to Name(secondArg()))
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
                val index = stats.indexOf { (statId, type, _ ) -> statId == idArg && type == secondArg() }
                stats.insert(index, Triple(idArg, secondArg(), thirdArg()))
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
                years += firstArg<UInt>()
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
    fun `rate movie once positive`() = runBlockingTest {
        source.rate(Inception, Rating.Positive)

        assert that source.ratedMovies().first().first *{
            +name equals Inception.name
            +actors `equals no order` Inception.actors
            +genres `equals no order` Inception.genres
            +year equals Inception.year
        }
    }
}
