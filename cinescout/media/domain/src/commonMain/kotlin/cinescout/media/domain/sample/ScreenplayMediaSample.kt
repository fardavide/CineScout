package cinescout.media.domain.sample

import cinescout.media.domain.model.MovieMedia

object ScreenplayMediaSample {

    val Inception = MovieMedia(
        backdrops = ScreenplayImagesSample.Inception.backdrops,
        posters = ScreenplayImagesSample.Inception.posters,
        videos = ScreenplayVideosSample.Inception.videos
    )

    val TheWolfOfWallStreet = MovieMedia(
        backdrops = ScreenplayImagesSample.TheWolfOfWallStreet.backdrops,
        posters = ScreenplayImagesSample.TheWolfOfWallStreet.posters,
        videos = ScreenplayVideosSample.TheWolfOfWallStreet.videos
    )
}
