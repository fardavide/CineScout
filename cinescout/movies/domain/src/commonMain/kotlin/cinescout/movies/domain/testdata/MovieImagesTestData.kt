package cinescout.movies.domain.testdata

import cinescout.common.model.TmdbBackdropImage
import cinescout.common.model.TmdbPosterImage
import cinescout.movies.domain.model.MovieImages

object MovieImagesTestData {

    val Inception = MovieImages(
        backdrops = listOf(TmdbBackdropImage("ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg")),
        movieId = TmdbMovieIdTestData.Inception,
        posters = listOf(TmdbPosterImage("8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg"))
    )

    val TheWolfOfWallStreet = MovieImages(
        backdrops = listOf(TmdbBackdropImage("blbA7NEHARQOWy5i9VF5K2kHrPc.jpg")),
        movieId = TmdbMovieIdTestData.TheWolfOfWallStreet,
        posters = listOf(TmdbPosterImage("pWHf4khOloNVfCxscsXFj3jj6gP.jpg"))
    )

    val War = MovieImages(
        backdrops = listOf(TmdbBackdropImage("5Tw0isY4Fs08burneYsa6JvHbER.jpg")),
        movieId = TmdbMovieIdTestData.War,
        posters = listOf(TmdbPosterImage("7JeHrXR1FU57Y6b90YDpFJMhmVO.jpg"))
    )
}
