package cinescout.database.model

sealed interface DatabaseTmdbScreenplayId {

    val value: Int
}

@JvmInline
value class DatabaseTmdbMovieId(override val value: Int) : DatabaseTmdbScreenplayId

@JvmInline
value class DatabaseTmdbTvShowId(override val value: Int) : DatabaseTmdbScreenplayId

