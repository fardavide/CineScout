package cinescout.database.model

sealed interface DatabaseScreenplayIds {

    val tmdb: DatabaseTmdbScreenplayId
    val trakt: DatabaseTraktScreenplayId
}

data class DatabaseMovieIds(
    override val tmdb: DatabaseTmdbMovieId,
    override val trakt: DatabaseTraktMovieId
) : DatabaseScreenplayIds

data class DatabaseTvShowIds(
    override val tmdb: DatabaseTmdbTvShowId,
    override val trakt: DatabaseTraktTvShowId
) : DatabaseScreenplayIds
