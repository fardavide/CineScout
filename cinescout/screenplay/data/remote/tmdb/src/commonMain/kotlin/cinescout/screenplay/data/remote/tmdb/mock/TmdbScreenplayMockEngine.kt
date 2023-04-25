package cinescout.screenplay.data.remote.tmdb.mock

import cinescout.network.testutil.respondJson
import cinescout.network.testutil.unhandled
import cinescout.screenplay.data.remote.tmdb.res.TmdbMovieRecommendationsJson
import cinescout.screenplay.data.remote.tmdb.res.TmdbScreenplayKeywordsJson
import cinescout.screenplay.data.remote.tmdb.res.TmdbTvShowRecommendationsJson
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.Url
import io.ktor.http.fullPath

fun TmdbScreenplayMockEngine() = MockEngine { requestData ->
    respondJson(getContent(requestData.url))
}

private fun getContent(url: Url): String {
    val fullPath = url.fullPath
    return when {
        "recommendations" in fullPath && "movie" in fullPath -> TmdbMovieRecommendationsJson.TwoMovies
        "recommendations" in fullPath && "tv" in fullPath -> TmdbTvShowRecommendationsJson.TwoTvShows

        "/${TmdbScreenplayIdSample.Avatar3.value}/keywords" in fullPath -> TmdbScreenplayKeywordsJson.Avatar3
        "/${TmdbScreenplayIdSample.BreakingBad.value}/keywords" in fullPath -> TmdbScreenplayKeywordsJson.BreakingBad
        "/${TmdbScreenplayIdSample.Dexter.value}/keywords" in fullPath -> TmdbScreenplayKeywordsJson.Dexter
        "/${TmdbScreenplayIdSample.Grimm.value}/keywords" in fullPath -> TmdbScreenplayKeywordsJson.Grimm
        "/${TmdbScreenplayIdSample.Inception.value}/keywords" in fullPath -> TmdbScreenplayKeywordsJson.Inception
        "/${TmdbScreenplayIdSample.TheWalkingDeadDeadCity.value}/keywords" in fullPath ->
            TmdbScreenplayKeywordsJson.TheWalkingDeadDeadCity
        "/${TmdbScreenplayIdSample.TheWolfOfWallStreet.value}/keywords" in fullPath ->
            TmdbScreenplayKeywordsJson.TheWolfOfWallStreet
        "/${TmdbScreenplayIdSample.War.value}/keywords" in fullPath -> TmdbScreenplayKeywordsJson.War

        else -> unhandled(url)
    }
}
