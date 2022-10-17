package cinescout.details.presentation

import cinescout.details.presentation.mapper.MovieDetailsUiModelMapper
import cinescout.details.presentation.mapper.TvShowDetailsUiModelMapper
import cinescout.details.presentation.viewmodel.MovieDetailsViewModel
import cinescout.details.presentation.viewmodel.TvShowDetailsViewModel
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.tvshows.domain.model.TmdbTvShowId
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
            getMovieMedia = get(),
            rateMovie = get(),
            removeMovieFromWatchlist = get()
        )
    }
    factory { TvShowDetailsUiModelMapper() }
    viewModel { (id: TmdbTvShowId) ->
        TvShowDetailsViewModel(
            addTvShowToWatchlist = get(),
            tvShowDetailsUiModelMapper = get(),
            tvShowId = id,
            networkErrorToMessageMapper = get(),
            getTvShowExtras = get(),
            getTvShowMedia = get(),
            rateTvShow = get(),
            removeTvShowFromWatchlist = get()
        )
    }
}
