package cinescout.details.presentation

import cinescout.details.presentation.viewmodel.MovieDetailsViewModel
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DetailsPresentationModule = module {

    viewModel { (id: TmdbMovieId) ->
        MovieDetailsViewModel(
            movieId = id,
            networkErrorToMessageMapper = get(),
            getMovieExtras = get()
        )
    }
}
