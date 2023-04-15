package cinescout.screenplay.domain.model

sealed interface TraktScreenplayId {

    @JvmInline
    value class Movie(val value: Int) : TraktScreenplayId

    @JvmInline
    value class TvShow(val value: Int) : TraktScreenplayId
}
