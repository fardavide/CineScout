package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieMedia
import cinescout.screenplay.domain.sample.ScreenplayImagesSample

object MovieMediaTestData {

    val Inception = MovieMedia(
        backdrops = ScreenplayImagesSample.Inception.backdrops,
        posters = ScreenplayImagesSample.Inception.posters,
        videos = MovieVideosTestData.Inception.videos
    )

    val TheWolfOfWallStreet = MovieMedia(
        backdrops = ScreenplayImagesSample.TheWolfOfWallStreet.backdrops,
        posters = ScreenplayImagesSample.TheWolfOfWallStreet.posters,
        videos = MovieVideosTestData.TheWolfOfWallStreet.videos
    )
}
