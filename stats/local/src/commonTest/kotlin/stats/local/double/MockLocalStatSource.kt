package stats.local.double

import database.movies.Movie
import database.stats.StatType
import entities.IntId
import entities.TmdbId
import entities.model.Name
import entities.model.Video
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import stats.LocalStatSource
import stats.local.LocalStatSourceImpl
import util.DispatchersProvider

fun mockLocalStatSource(): LocalStatSource {
    val actors = mutableListOf<Pair<TmdbId, Name>>()
    val genres = mutableListOf<Pair<TmdbId, Name>>()
    val movies = mutableListOf<Movie>()
    val movieActors = mutableListOf<Pair<IntId, IntId>>()
    val movieGenres = mutableListOf<Pair<IntId, IntId>>()
    val movieVideos = mutableListOf<Pair<IntId, IntId>>()
    val stats = mutableListOf<Triple<IntId, StatType, Int>>()
    val suggestions = mutableSetOf<IntId>()
    val videos = mutableListOf<Video>()
    val watchlist = mutableListOf<IntId>()

    return spyk(
        LocalStatSourceImpl(
            dispatchers = object : DispatchersProvider {
                override val Main = Dispatchers.Main
                override val Comp = Dispatchers.Main
                override val Io = Dispatchers.Main
            },
            actors = mockActorQueries(actors),
            genres = mockGenreQueries(genres),
            movies = mockMovieQueries(
                movies,
                movieActors,
                movieGenres,
                movieVideos,
                actors,
                genres,
                stats,
                suggestions,
                videos,
                watchlist
            ),
            movieActors = mockMovieActorQueries(movieActors),
            movieGenres = mockMovieGenreQueries(movieGenres),
            stats = mockStatQueries(actors, genres, movies, stats),
            suggestions = mockSuggestionQueries(suggestions),
            watchlist = mockWatchlistQueries(watchlist),
            years = mockYearRangeQueries(),
        )
    )
}

fun <T> Collection<T>.indexOf(find: (T) -> Boolean) = indexOfFirst(find).takeIf { it >= 0 }

fun <T> MutableList<T>.insert(index: Int?, element: T) {
    if (index != null) this[index] = element
    else this += element
}
