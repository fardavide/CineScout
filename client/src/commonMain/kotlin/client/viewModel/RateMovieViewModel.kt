package client.viewModel

import client.ViewStateFlow
import domain.FindMovie
import domain.stats.AddMovieToWatchlist
import domain.stats.RateMovie
import entities.TmdbId
import entities.model.UserRating
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RateMovieViewModel(
    override val scope: CoroutineScope,
    private val addMovieToWatchlist: AddMovieToWatchlist,
    private val rateMovie: RateMovie,
    private val findMovie: FindMovie
): CineViewModel {

    val result = ViewStateFlow<Unit>()

    operator fun set(id: TmdbId, rating: UserRating) =
        rate(id, rating)

    fun rate(id: TmdbId, rating: UserRating) {
        scope.launch {
            result.emitCatching(initLoading = true) {
                val movie = requireNotNull(findMovie(id)) { "Cannot load movie with id '${id.i}'" }
                rateMovie(movie, rating)
            }
        }
    }

    operator fun set(movie: Movie, rating: UserRating) =
        rate(movie, rating)

    fun rate(movie: Movie, rating: UserRating) {
        scope.launch {
            result.emitCatching(initLoading = true) {
                rateMovie(movie, rating)
            }
        }
    }

    infix fun addToWatchlist(movie: Movie) {
        scope.launch {
            result.emitCatching(initLoading = true) {
                addMovieToWatchlist(movie)
            }
        }
    }
}
