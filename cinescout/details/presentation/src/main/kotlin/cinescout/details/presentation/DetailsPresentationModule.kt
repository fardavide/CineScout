package cinescout.details.presentation

import cinescout.details.presentation.mapper.MovieDetailsUiModelMapper
import cinescout.details.presentation.viewmodel.MovieDetailsViewModel
import cinescout.movies.domain.model.TmdbMovieId
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val DetailsPresentationModule = module {

    factory { MovieDetailsUiModelMapper() }
    viewModel { (id: TmdbMovieId) ->
        MovieDetailsViewModel(
            addMovieToWatchlist = get(),
            movieDetailsUiModelMapper = get(),
            movieId = id,
            networkErrorToMessageMapper = get(),
            getMovieExtras = get(),
            rateMovie = get(),
            removeMovieFromWatchlist = get()
        )
    }
}
