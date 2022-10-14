package cinescout.tvshows.data

import cinescout.tvshows.domain.TvShowRepository
import org.koin.dsl.module

val TvShowsDataModule = module {

    factory<TvShowRepository> {
        RealTvShowRepository(
            localTvShowDataSource = get(),
            remoteTvShowDataSource = get(),
            storeOwner = get()
        )
    }
}
