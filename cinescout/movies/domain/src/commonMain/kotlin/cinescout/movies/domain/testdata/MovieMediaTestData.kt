package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieMedia

object MovieMediaTestData {

    val Inception = MovieMedia(
        backdrops = MovieImagesTestData.Inception.backdrops,
        posters = MovieImagesTestData.Inception.posters,
        videos = MovieVideosTestData.Inception.videos
    )

    val TheWolfOfWallStreet = MovieMedia(
        backdrops = MovieImagesTestData.TheWolfOfWallStreet.backdrops,
        posters = MovieImagesTestData.TheWolfOfWallStreet.posters,
        videos = MovieVideosTestData.TheWolfOfWallStreet.videos
    )
}
