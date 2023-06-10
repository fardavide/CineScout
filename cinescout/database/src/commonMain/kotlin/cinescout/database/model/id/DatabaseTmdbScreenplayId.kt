package cinescout.database.model.id

sealed interface DatabaseTmdbScreenplayId {

    val value: Int
}

@JvmInline
value class DatabaseTmdbMovieId(override val value: Int) : DatabaseTmdbScreenplayId

@JvmInline
value class DatabaseTmdbTvShowId(override val value: Int) : DatabaseTmdbScreenplayId
