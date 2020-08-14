package stats.local

import database.Database
import database.databaseModule
import org.koin.dsl.module
import stats.LocalStatSource
import stats.statsModule

val localStatsModule = module {

    factory<LocalStatSource> {
        LocalStatSourceImpl(
            actors = get(),
            genres = get(),
            movies = get(),
            movieActors = get(),
            movieGenres = get(),
            stats = get(),
            years = get(),
        )
    }

    factory { get<Database>().actorQueries }
    factory { get<Database>().genreQueries }
    factory { get<Database>().movieQueries }
    factory { get<Database>().movie_actorQueries }
    factory { get<Database>().movie_genreQueries }
    factory { get<Database>().statQueries }
    factory { get<Database>().yearRangeQueries }

} + databaseModule + statsModule
