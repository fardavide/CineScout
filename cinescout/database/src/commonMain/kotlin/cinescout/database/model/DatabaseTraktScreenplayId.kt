package cinescout.database.model

sealed interface DatabaseTraktScreenplayId {

    val value: Int
}

@JvmInline
value class DatabaseTraktMovieId(override val value: Int) : DatabaseTraktScreenplayId

@JvmInline
value class DatabaseTraktTvShowId(override val value: Int) : DatabaseTraktScreenplayId
