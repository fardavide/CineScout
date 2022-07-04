package cinescout.database

import cinescout.database.adapter.RatingAdapter
import cinescout.database.adapter.TmdbIdAdapter
import org.koin.dsl.module

val DatabaseAdapterModule = module {

    factory { Movie.Adapter(tmdbIdAdapter = TmdbIdAdapter) }
    factory { MovieRating.Adapter(tmdbIdAdapter = TmdbIdAdapter, ratingAdapter = RatingAdapter) }
    factory { Watchlist.Adapter(tmdbIdAdapter = TmdbIdAdapter) }
}
