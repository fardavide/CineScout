package cinescout.database.model

sealed interface DatabaseTmdbScreenplayId {

    val value: Int

    @JvmInline
    value class Movie(override val value: Int) : DatabaseTmdbScreenplayId

    @JvmInline
    value class TvShow(override val value: Int) : DatabaseTmdbScreenplayId
}
