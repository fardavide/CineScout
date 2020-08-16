package client.viewModel

import client.DispatchersProvider
import client.ViewStateFlow
import domain.RateMovie
import entities.Rating
import entities.movies.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RateMovieViewModel(
    override val scope: CoroutineScope,
    dispatchers: DispatchersProvider,
    private val rateMovie: RateMovie
): CineViewModel, DispatchersProvider by dispatchers {

    val result = ViewStateFlow<Unit>()

    operator fun set(movie: Movie, rating: Rating) =
        rate(movie, rating)

    fun rate(movie: Movie, rating: Rating) {
        scope.launch {
            result.emitCatching(initLoading = true) {
                rateMovie(movie, rating)
            }
        }
    }
}
