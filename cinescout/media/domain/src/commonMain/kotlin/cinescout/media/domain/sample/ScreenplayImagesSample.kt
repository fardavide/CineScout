package cinescout.media.domain.sample

import cinescout.media.domain.model.MovieImages
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.media.domain.model.TvShowImages
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample

object ScreenplayImagesSample {

    val BreakingBad = TvShowImages(
        backdrops = listOf(
            TmdbBackdropImage("84XPpjGvxNyExjSuLQe0SzioErt.jpg"),
            TmdbBackdropImage("n3u3kgNttY1F5Ixi5bMY9BwZImQ.jpg"),
            TmdbBackdropImage("yXSzo0VU1Q1QaB7Xg5Hqe4tXXA3.jpg")
        ),
        posters = listOf(
            TmdbPosterImage("ggFHVNu6YYI5L9pCfOacjizRGt.jpg"),
            TmdbPosterImage("ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg"),
            TmdbPosterImage("yQAh12bfMjMRaGJupcKt5T5dUhz.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.BreakingBad
    )

    val Dexter = TvShowImages(
        backdrops = listOf(
            TmdbBackdropImage("fIKc2cR1GglarzChMAb4BOP1qHP.jpg"),
            TmdbBackdropImage("hHAxmPhF9jg67ufl9WQakwzcCtL.jpg"),
            TmdbBackdropImage("2CWQEkzBwM5Spb5nyZD8XfMBwM3.jpg")
        ),
        posters = listOf(
            TmdbPosterImage("58H6Ctze1nnpS0s9vPmAAzPcipR.jpg"),
            TmdbPosterImage("eKhgbGjY219deSAVh5OOY3DR3Ao.jpg"),
            TmdbPosterImage("9uTF9PMK7uHxfrXvc5IJmG0SPv.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.Dexter
    )

    val Grimm = TvShowImages(
        backdrops = listOf(
            TmdbBackdropImage("oS3nip9GGsx5A7vWp8A1cazqJlF.jpg"),
            TmdbBackdropImage("opyyC62L5N1nHsOVoEwc84Q45B5.jpg"),
            TmdbBackdropImage("nQ91HWUIqCwBeyP1Bw2b0SjWYY0.jpg")
        ),
        posters = listOf(
            TmdbPosterImage("iOptnt1QHi6bIHmOq6adnZTV0bU.jpg"),
            TmdbPosterImage("40Lrj8AKZhGrEmbYbgLbHkqPZvq.jpg"),
            TmdbPosterImage("5hC8CertBqHbXNPcfm1LZ18VcjD.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.Grimm
    )

    val Inception = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage("s3TBrRGB1iav7gFOCNx3H31MoES.jpg"),
            TmdbBackdropImage("ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg"),
            TmdbBackdropImage("2HmLvOvu1rhfxK50WfJ4jFKy9zQ.jpg")
        ),
        posters = listOf(
            TmdbPosterImage("8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg"),
            TmdbPosterImage("edv5CZvWj09upOsy2Y6IwDhK8bt.jpg"),
            TmdbPosterImage("9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.Inception
    )

    val TheWolfOfWallStreet = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage("cWUOv3H7YFwvKeaQhoAQTLLpo9Z.jpg"),
            TmdbBackdropImage("br7n8b3ELexcvs6l30IH2x9P2ux.jpg"),
            TmdbBackdropImage("74pgiTEwbgKdPBkhR000wQd1ywI.jpg")
        ),
        posters = listOf(
            TmdbPosterImage("jTlIYjvS16XOpsfvYCTmtEHV10K.jpg"),
            TmdbPosterImage("9XlgIt4LOW222cuahn33qhsDBqD.jpg"),
            TmdbPosterImage("dQIQZbJXn1pflQw3nwvXLJX0dHa.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet
    )

    val War = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage("5Tw0isY4Fs08burneYsa6JvHbER.jpg"),
            TmdbBackdropImage("aOSDKvqglKVa3SYy4CPXYUAfDlf.jpg"),
            TmdbBackdropImage("ecthziUIQorlB3BI3N19Ks20QNV.jpg")
        ),
        posters = listOf(
            TmdbPosterImage("7JeHrXR1FU57Y6b90YDpFJMhmVO.jpg"),
            TmdbPosterImage("hBW1ZGu72cHOoRSnFrIc2Y9UU7f.jpg"),
            TmdbPosterImage("dVfkp6Miu0xLVpzSIkJwvOHgMrx.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.War
    )
}
