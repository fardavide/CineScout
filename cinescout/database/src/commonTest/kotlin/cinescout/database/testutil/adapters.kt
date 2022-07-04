package cinescout.database.testutil

import cinescout.database.Movie
import cinescout.database.adapter.TmdbIdAdapter

val MovieAdapter = Movie.Adapter(tmdbIdAdapter = TmdbIdAdapter)
