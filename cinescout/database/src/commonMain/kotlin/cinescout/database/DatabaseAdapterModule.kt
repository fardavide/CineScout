package cinescout.database

import cinescout.database.adapter.TmdbIdAdapter
import org.koin.dsl.module

val DatabaseAdapterModule = module {

    factory { Movie.Adapter(tmdbIdAdapter = TmdbIdAdapter) }
}
