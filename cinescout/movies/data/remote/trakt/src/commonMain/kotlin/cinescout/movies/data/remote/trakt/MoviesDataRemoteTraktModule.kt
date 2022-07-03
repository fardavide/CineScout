package cinescout.movies.data.remote.trakt

import cinescout.movies.data.remote.TraktRemoteMovieDataSource
import cinescout.network.trakt.TraktNetworkQualifier
import org.koin.dsl.module

val MoviesDataRemoteTraktModule = module {

    factory<TraktRemoteMovieDataSource> { RealTraktMovieDataSource(client = get(TraktNetworkQualifier.Client)) }
}
