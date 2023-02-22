package cinescout.tvshows.data.remote.trakt.testutil

import cinescout.tvshows.data.remote.trakt.model.GetRatings
import cinescout.tvshows.domain.sample.TvShowSample
import cinescout.tvshows.domain.sample.TvShowWithPersonalRatingSample

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
                "${GetRatings.Result.Rating}": ${TvShowWithPersonalRatingSample.Grimm.personalRating.value}
            }
        ]
    """.trimIndent()
}
