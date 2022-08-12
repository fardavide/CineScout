package cinescout.database

import org.koin.core.scope.Scope
import org.koin.dsl.module

val DatabaseQueriesModule = module {

    factory { database.likedMovieQueries }
    factory { database.genreQueries }
    factory { database.keywordQueries }
    factory { database.movieCastMemberQueries }
    factory { database.movieCrewMemberQueries }
    factory { database.movieGenreQueries }
    factory { database.movieKeywordQueries }
    factory { database.movieQueries }
    factory { database.movieRatingQueries }
    factory { database.personQueries }
    factory { database.suggestedMovieQueries }
    factory { database.tmdbAccountQueries }
    factory { database.tmdbAuthStateQueries }
    factory { database.traktAccountQueries }
    factory { database.traktAuthStateQueries }
    factory { database.watchlistQueries }
}

private val Scope.database get() = get<Database>()

