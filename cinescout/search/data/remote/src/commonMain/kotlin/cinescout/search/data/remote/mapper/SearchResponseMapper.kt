package cinescout.search.data.remote.mapper

import cinescout.screenplay.data.remote.tmdb.mapper.TmdbScreenplayMapper
import cinescout.screenplay.domain.model.Screenplay
import cinescout.search.data.remote.model.SearchMovieResponse
import cinescout.search.data.remote.model.SearchTvShowResponse
import org.koin.core.annotation.Factory

@Factory
internal class SearchResponseMapper(
    private val screenplayMapper: TmdbScreenplayMapper
) {

    fun toScreenplays(response: SearchMovieResponse): List<Screenplay> = response.results.map { pageResult ->
        screenplayMapper.toMovie(pageResult.toTmdbMovie())
    }

    fun toScreenplays(response: SearchTvShowResponse): List<Screenplay> = response.results.map { pageResult ->
        screenplayMapper.toTvShow(pageResult.toTmdbTvShow())
    }
}
