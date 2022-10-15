package cinescout.tvshows.data.remote.trakt.testutil

import cinescout.tvshows.data.remote.trakt.model.GetRatings
import cinescout.tvshows.domain.testdata.TvShowTestData
import cinescout.tvshows.domain.testdata.TvShowWithPersonalRatingTestData

object TraktTvShowsRatingJson {

    val OneTvShow = """
        [
            {
                "${GetRatings.Result.TvShowType}": {
                    "${GetRatings.Result.Title}": "${TvShowTestData.Grimm.title}",
                    "${GetRatings.Result.Ids}": {
                        "${GetRatings.Result.Ids.Tmdb}": ${TvShowTestData.Grimm.tmdbId.value}
                    }
                },
                "${GetRatings.Result.Rating}": ${TvShowWithPersonalRatingTestData.Grimm.personalRating.value}
            }
        ]
    """
}
