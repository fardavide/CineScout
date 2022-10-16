package cinescout.tvshows.data.remote

import cinescout.tvshows.data.RemoteTvShowDataSource
import org.koin.dsl.module

val TvShowsDataRemoteModule = module {

    factory<RemoteTvShowDataSource> {
        RealRemoteTvShowDataSource(
            dualSourceCall = get(),
            tmdbSource = get(),
            traktSource = get()
        )
    }
}
