package cinescout.media.domain.sample

import cinescout.media.domain.model.MovieImages
import cinescout.media.domain.model.TmdbBackdropImage
import cinescout.media.domain.model.TmdbPosterImage
import cinescout.media.domain.model.TvShowImages
import cinescout.screenplay.domain.sample.TmdbScreenplayIdSample

object ScreenplayImagesSample {

    val BreakingBad = TvShowImages(
        backdrops = listOf(
            TmdbBackdropImage(isPrimary = true, "84XPpjGvxNyExjSuLQe0SzioErt.jpg"),
            TmdbBackdropImage(isPrimary = false, "n3u3kgNttY1F5Ixi5bMY9BwZImQ.jpg"),
            TmdbBackdropImage(isPrimary = false, "yXSzo0VU1Q1QaB7Xg5Hqe4tXXA3.jpg")
        ),
        posters = listOf(
            TmdbPosterImage(isPrimary = true, "ggFHVNu6YYI5L9pCfOacjizRGt.jpg"),
            TmdbPosterImage(isPrimary = false, "ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg"),
            TmdbPosterImage(isPrimary = false, "yQAh12bfMjMRaGJupcKt5T5dUhz.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.BreakingBad
    )

    val Dexter = TvShowImages(
        backdrops = listOf(
            TmdbBackdropImage(isPrimary = true, "fIKc2cR1GglarzChMAb4BOP1qHP.jpg"),
            TmdbBackdropImage(isPrimary = false, "hHAxmPhF9jg67ufl9WQakwzcCtL.jpg"),
            TmdbBackdropImage(isPrimary = false, "2CWQEkzBwM5Spb5nyZD8XfMBwM3.jpg")
        ),
        posters = listOf(
            TmdbPosterImage(isPrimary = true, "58H6Ctze1nnpS0s9vPmAAzPcipR.jpg"),
            TmdbPosterImage(isPrimary = false, "eKhgbGjY219deSAVh5OOY3DR3Ao.jpg"),
            TmdbPosterImage(isPrimary = false, "9uTF9PMK7uHxfrXvc5IJmG0SPv.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.Dexter
    )

    val Grimm = TvShowImages(
        backdrops = listOf(
            TmdbBackdropImage(isPrimary = true, "oS3nip9GGsx5A7vWp8A1cazqJlF.jpg"),
            TmdbBackdropImage(isPrimary = false, "opyyC62L5N1nHsOVoEwc84Q45B5.jpg"),
            TmdbBackdropImage(isPrimary = false, "nQ91HWUIqCwBeyP1Bw2b0SjWYY0.jpg")
        ),
        posters = listOf(
            TmdbPosterImage(isPrimary = true, "iOptnt1QHi6bIHmOq6adnZTV0bU.jpg"),
            TmdbPosterImage(isPrimary = false, "40Lrj8AKZhGrEmbYbgLbHkqPZvq.jpg"),
            TmdbPosterImage(isPrimary = false, "5hC8CertBqHbXNPcfm1LZ18VcjD.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.Grimm
    )

    val Inception = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage(isPrimary = true, "s3TBrRGB1iav7gFOCNx3H31MoES.jpg"),
            TmdbBackdropImage(isPrimary = false, "ztZ4vw151mw04Bg6rqJLQGBAmvn.jpg"),
            TmdbBackdropImage(isPrimary = false, "2HmLvOvu1rhfxK50WfJ4jFKy9zQ.jpg")
        ),
        posters = listOf(
            TmdbPosterImage(isPrimary = true, "8IB2e4r4oVhHnANbnm7O3Tj6tF8.jpg"),
            TmdbPosterImage(isPrimary = false, "edv5CZvWj09upOsy2Y6IwDhK8bt.jpg"),
            TmdbPosterImage(isPrimary = false, "9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.Inception
    )

    val TheWolfOfWallStreet = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage(isPrimary = true, "cWUOv3H7YFwvKeaQhoAQTLLpo9Z.jpg"),
            TmdbBackdropImage(isPrimary = false, "br7n8b3ELexcvs6l30IH2x9P2ux.jpg"),
            TmdbBackdropImage(isPrimary = false, "74pgiTEwbgKdPBkhR000wQd1ywI.jpg")
        ),
        posters = listOf(
            TmdbPosterImage(isPrimary = true, "jTlIYjvS16XOpsfvYCTmtEHV10K.jpg"),
            TmdbPosterImage(isPrimary = false, "9XlgIt4LOW222cuahn33qhsDBqD.jpg"),
            TmdbPosterImage(isPrimary = false, "dQIQZbJXn1pflQw3nwvXLJX0dHa.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.TheWolfOfWallStreet
    )

    val War = MovieImages(
        backdrops = listOf(
            TmdbBackdropImage(isPrimary = true, "5Tw0isY4Fs08burneYsa6JvHbER.jpg"),
            TmdbBackdropImage(isPrimary = false, "aOSDKvqglKVa3SYy4CPXYUAfDlf.jpg"),
            TmdbBackdropImage(isPrimary = false, "ecthziUIQorlB3BI3N19Ks20QNV.jpg")
        ),
        posters = listOf(
            TmdbPosterImage(isPrimary = true, "7JeHrXR1FU57Y6b90YDpFJMhmVO.jpg"),
            TmdbPosterImage(isPrimary = false, "hBW1ZGu72cHOoRSnFrIc2Y9UU7f.jpg"),
            TmdbPosterImage(isPrimary = false, "dVfkp6Miu0xLVpzSIkJwvOHgMrx.jpg")
        ),
        screenplayId = TmdbScreenplayIdSample.War
    )
}
