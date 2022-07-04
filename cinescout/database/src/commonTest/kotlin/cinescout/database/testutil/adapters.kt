package cinescout.database.testutil

import cinescout.database.Movie
import cinescout.database.MovieRating
import cinescout.database.Watchlist
import cinescout.database.adapter.RatingAdapter
import cinescout.database.adapter.TmdbIdAdapter

val MovieAdapter = Movie.Adapter(tmdbIdAdapter = TmdbIdAdapter)
val MovieRatingAdapter = MovieRating.Adapter(tmdbIdAdapter = TmdbIdAdapter, ratingAdapter = RatingAdapter)
val WatchlistAdapter = Watchlist.Adapter(tmdbIdAdapter = TmdbIdAdapter)
