package cinescout.progress.domain.usecase

import cinescout.history.domain.model.MovieHistory
import cinescout.progress.domain.model.MovieProgress
import cinescout.screenplay.domain.model.Movie
import cinescout.utils.kotlin.ComputationDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
class CalculateProgress(
    @Named(ComputationDispatcher) private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(movie: Movie, history: MovieHistory): MovieProgress =
        withContext(dispatcher) {
            check(movie.ids == history.screenplayIds) { "Movie and history must have the same ids" }

            when (history.items.isEmpty()) {
                true -> MovieProgress.Unwatched(movie)
                false -> MovieProgress.Watched(
                    screenplay = movie,
                    count = history.items.size
                )
            }
        }
}
