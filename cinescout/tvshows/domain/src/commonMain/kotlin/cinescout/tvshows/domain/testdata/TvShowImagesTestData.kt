package cinescout.tvshows.domain.testdata

import cinescout.screenplay.domain.model.TmdbBackdropImage
import cinescout.screenplay.domain.model.TmdbPosterImage
import cinescout.tvshows.domain.model.TvShowImages
import cinescout.tvshows.domain.sample.TmdbTvShowIdSample

object TvShowImagesTestData {

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
        tvShowId = TmdbTvShowIdSample.BreakingBad
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
        tvShowId = TmdbTvShowIdSample.Dexter
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
        tvShowId = TmdbTvShowIdSample.Grimm
    )
}
