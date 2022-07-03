package cinescout.movies.domain.usecase

import cinescout.movies.domain.model.Movie
import cinescout.movies.domain.model.Rating

class RateMovie {

    suspend operator fun invoke(movie: Movie, rating: Rating) {

    }
}
