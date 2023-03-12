package cinescout.screenplay.domain.sample

import cinescout.screenplay.domain.model.MovieImages
import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage

object ScreenplayImagesSample {

    val Inception = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage("s3TBrRGB1iav7gFOCNx3H31MoES.jpg"),
            TmdbBackdropImage("ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg"),
            TmdbBackdropImage("2HmLvOvu1rhfxK50WfJ4jFKy9zQ.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.Inception,
        posters = listOf(
            TmdbPosterImage("8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg"),
            TmdbPosterImage("edv5CZvWj09upOsy2Y6IwDhK8bt.jpg"),
            TmdbPosterImage("9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg")
        )
    )

    val TheWolfOfWallStreet = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage("cWUOv3H7YFwvKeaQhoAQTLLpo9Z.jpg"),
            TmdbBackdropImage("br7n8b3ELexcvs6l30IH2x9P2ux.jpg"),
            TmdbBackdropImage("74pgiTEwbgKdPBkhR000wQd1ywI.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet,
        posters = listOf(
            TmdbPosterImage("jTlIYjvS16XOpsfvYCTmtEHV10K.jpg"),
            TmdbPosterImage("9XlgIt4LOW222cuahn33qhsDBqD.jpg"),
            TmdbPosterImage("dQIQZbJXn1pflQw3nwvXLJX0dHa.jpg")
        )
    )

    val War = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage("5Tw0isY4Fs08burneYsa6JvHbER.jpg"),
            TmdbBackdropImage("aOSDKvqglKVa3SYy4CPXYUAfDlf.jpg"),
            TmdbBackdropImage("ecthziUIQorlB3BI3N19Ks20QNV.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.War,
        posters = listOf(
            TmdbPosterImage("7JeHrXR1FU57Y6b90YDpFJMhmVO.jpg"),
            TmdbPosterImage("hBW1ZGu72cHOoRSnFrIc2Y9UU7f.jpg"),
            TmdbPosterImage("dVfkp6Miu0xLVpzSIkJwvOHgMrx.jpg")
        )
    )
}