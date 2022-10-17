package cinescout.tvshows.domain.testdata

import cinescout.tvshows.domain.model.TvShowMedia

object TvShowMediaTestData {

    val BreakingBad = TvShowMedia(
        backdrops = TvShowImagesTestData.BreakingBad.backdrops,
        posters = TvShowImagesTestData.BreakingBad.posters,
        videos = TvShowVideosTestData.BreakingBad.videos
    )

    val Grimm = TvShowMedia(
        backdrops = TvShowImagesTestData.Grimm.backdrops,
        posters = TvShowImagesTestData.Grimm.posters,
        videos = TvShowVideosTestData.Grimm.videos
    )
}
