package stats.local

import database.Database
import database.databaseModule
import org.koin.dsl.module
import stats.LocalStatSource
import stats.statsModule

@Suppress("DuplicatedCode")
val localStatsModule = module {

    factory<LocalStatSource> {
        LocalStatSourceImpl(
            dispatchers = get(),
            actors = get(),
            genres = get(),
            movies = get(),
            movieActors = get(),
            movieGenres = get(),
            stats = get(),
            watchlist = get(),
            years = get(),
        )
    }

    factory { get<Database>().actorQueries }
    factory { get<Database>().genreQueries }
    factory { get<Database>().movieQueries }
    factory { get<Database>().movie_actorQueries }
    factory { get<Database>().movie_genreQueries }
    factory { get<Database>().statQueries }
    factory { get<Database>().watchlistQueries }
    factory { get<Database>().yearRangeQueries }

} + databaseModule + statsModule

