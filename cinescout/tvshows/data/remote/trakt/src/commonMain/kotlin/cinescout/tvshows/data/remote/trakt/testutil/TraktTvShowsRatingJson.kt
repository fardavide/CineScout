package cinescout.tvshows.data.remote.trakt.testutil

import cinescout.tvshows.data.remote.trakt.model.GetRatings
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.testdata.TvShowWithPersonalRatingTestData

object TraktTvShowsRatingJson {

    val OneTvShow = """
        [
            {
                "${GetRatings.Result.TvShowType}": {
                    "${GetRatings.Result.Title}": "${TvShowSample.Grimm.title}",
                    "${GetRatings.Result.Ids}": {
                        "${GetRatings.Result.Ids.Tmdb}": ${TvShowSample.Grimm.tmdbId.value}
                    }
                },
                "${GetRatings.Result.Rating}": ${TvShowWithPersonalRatingTestData.Grimm.personalRating.value}
            }
        ]
    """
}
