package cinescout.database.model

sealed interface DatabaseTmdbScreenplayId {

    val value: Int

    companion object {

        const val TypeMovie = "movie"
        const val TypeTvShow = "tv-show"
        const val ValueSeparator = "_"
    }
}

@JvmInline
value class DatabaseTmdbMovieId(override val value: Int) : DatabaseTmdbScreenplayId

@JvmInline
value class DatabaseTmdbTvShowId(override val value: Int) : DatabaseTmdbScreenplayId

