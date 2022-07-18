package cinescout.movies.data.remote.trakt

import cinescout.movies.data.remote.TraktRemoteMovieDataSource
import cinescout.movies.data.remote.trakt.mapper.TraktMovieMapper
import cinescout.movies.data.remote.trakt.service.TraktMovieService
import cinescout.network.trakt.TraktNetworkQualifier
import org.koin.dsl.module

val MoviesDataRemoteTraktModule = module {

    factory { TraktMovieMapper() }
    factory { TraktMovieService(client = get(TraktNetworkQualifier.Client)) }
    factory<TraktRemoteMovieDataSource> { RealTraktMovieDataSource(movieMapper = get(), service = get()) }
}
