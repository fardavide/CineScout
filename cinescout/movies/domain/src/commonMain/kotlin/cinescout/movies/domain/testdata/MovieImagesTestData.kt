package cinescout.movies.domain.testdata

import cinescout.common.model.TmdbBackdropImage
import cinescout.movies.domain.model.MovieImages
import cinescout.movies.domain.model.TmdbPosterImage

object MovieImagesTestData {

    val Inception = MovieImages(
        backdrops = listOf(TmdbBackdropImage("ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg")),
        movieId = MovieTestData.Inception.tmdbId,
        posters = listOf(TmdbPosterImage("8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg"))
    )

    val TheWolfOfWallStreet = MovieImages(
        backdrops = listOf(TmdbBackdropImage("blbA7NEHARQOWy5i9VF5K2kHrPc.jpg")),
        movieId = MovieTestData.TheWolfOfWallStreet.tmdbId,
        posters = listOf(TmdbPosterImage("pWHf4khOloNVfCxscsXFj3jj6gP.jpg"))
    )
}
