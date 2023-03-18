package cinescout.media.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ScreenplayImages {

    val backdrops: List<TmdbBackdropImage>
    val posters: List<TmdbPosterImage>
    val screenplayId: TmdbScreenplayId
    
    fun primaryBackdrop(): TmdbBackdropImage? =
        backdrops.firstOrNull { it.isPrimary } ?: backdrops.firstOrNull()
    
    fun primaryPoster(): TmdbPosterImage? = posters.firstOrNull { it.isPrimary } ?: posters.firstOrNull()
}

fun ScreenplayImages(
    backdrops: List<TmdbBackdropImage>,
    posters: List<TmdbPosterImage>,
    screenplayId: TmdbScreenplayId
) = when (screenplayId) {
    is TmdbScreenplayId.Movie -> MovieImages(
        backdrops = backdrops,
        posters = posters,
        screenplayId = screenplayId
    )
    is TmdbScreenplayId.TvShow -> TvShowImages(
        backdrops = backdrops,
        posters = posters,
        screenplayId = screenplayId
    )
}

data class MovieImages(
    override val backdrops: List<TmdbBackdropImage>,
    override val posters: List<TmdbPosterImage>,
    override val screenplayId: TmdbScreenplayId.Movie
) : ScreenplayImages

data class TvShowImages(
    override val backdrops: List<TmdbBackdropImage>,
    override val posters: List<TmdbPosterImage>,
    override val screenplayId: TmdbScreenplayId.TvShow
) : ScreenplayImages
