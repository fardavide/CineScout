package client.viewModel

import client.DispatchersProvider
import client.ViewStateFlow
import domain.FindMovie
import domain.RateMovie
import entities.Rating
import entities.TmdbId
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RateMovieViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val rateMovie: RateMovie,
    private val findMovie: FindMovie
): CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Unit>()

    operator fun set(id: TmdbId, rating: Rating) =
        rate(id, rating)

    fun rate(id: TmdbId, rating: Rating) {
        scope.launch(Io) {
            result.emitCatching(initLoading = true) {
                val movie = requireNotNull(findMovie(id)) { "Cannot load movie with id '${id.i}'" }
                rateMovie(movie, rating)
            }
        }
    }

    operator fun set(movie: Movie, rating: Rating) =
        rate(movie, rating)

    fun rate(movie: Movie, rating: Rating) {
        scope.launch(Io) {
            result.emitCatching(initLoading = true) {
                rateMovie(movie, rating)
            }
        }
    }
}
