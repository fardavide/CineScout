package stats.local.double

import database.movies.Movie
import database.stats.StatQueries
import database.stats.StatType
import entities.IntId
import entities.TmdbId
import entities.model.Name
import io.mockk.every
import io.mockk.mockk

fun mockStatQueries(
    actors: MutableList<Pair<TmdbId, Name>>,
    genres: MutableList<Pair<TmdbId, Name>>,
    movies: MutableList<Movie>,
    stats: MutableList<Triple<IntId, StatType, Int>>
): StatQueries = mockk {

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
                stats.find { (statId, type, _) -> statId == intId && type == StatType.ACTOR }?.third
            }
        }
    }

    // selectActorRatingByTmdbId
    every { selectActorRatingByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                val id = actors.indexOf { it.first == tmdbIdArg }?.let(::IntId)
                stats.find { (statId, type, _) -> statId == id && type == StatType.ACTOR }?.third
            }
        }
    }

    // selectGenreRating
    every { selectGenreRating(IntId(any())) } answers {
        val intId = IntId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                stats.find { (statId, type, _) -> statId == intId && type == StatType.GENRE }?.third
            }
        }
    }

    // selectGenreRatingByTmdbId
    every { selectGenreRatingByTmdbId(TmdbId(any())) } answers {
        val tmdbIdArg = TmdbId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                val id = genres.indexOf { it.first == tmdbIdArg }?.let(::IntId)
                stats.find { (statId, type, _) -> statId == id && type == StatType.GENRE }?.third
            }
        }
    }

    // selectMovieRating
    every { selectMovieRating(IntId(any())) } answers {
        val intId = IntId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                stats.find { (statId, type, _) -> statId == intId && type == StatType.MOVIE }?.third
            }
        }
    }

    // selectMovieRatingByTmdbId
    every { selectMovieRatingByTmdbId(TmdbId(any())) } answers {
        val tmdbId = TmdbId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                val id = movies.indexOf { it.tmdbId == tmdbId }?.let(::IntId)
                stats.find { (statId, type, _) -> statId == id && type == StatType.MOVIE }?.third
            }
        }
    }

    // selectYearRating
    every { selectYearRating(IntId(any())) } answers {
        val intId = IntId(firstArg())
        mockk {
            every { executeAsOneOrNull() } answers {
                stats.find { (statId, type, _) -> statId == intId && type == StatType.FIVE_YEAR_RANGE }?.third
            }
        }
    }

    every { selectYearRatingById(any<Int>().toUInt()) } answers {
        mockk {
            val intId = IntId(firstArg())
            every { executeAsOneOrNull() } answers {
                stats.find { (statId, type) -> statId == intId && type == StatType.FIVE_YEAR_RANGE }?.third
            }
        }
    }
}
