package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieMedia

object MovieMediaTestData {

    val Inception = MovieMedia(
        backdrops = MovieImagesTestData.Inception.backdrops,
        posters = MovieImagesTestData.Inception.posters
    )

    val TheWolfOfWallStreet = MovieMedia(
        backdrops = MovieImagesTestData.TheWolfOfWallStreet.backdrops,
        posters = MovieImagesTestData.TheWolfOfWallStreet.posters
    )
}
