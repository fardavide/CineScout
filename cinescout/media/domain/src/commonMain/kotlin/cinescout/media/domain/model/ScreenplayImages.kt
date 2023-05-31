package cinescout.media.domain.model

import cinescout.screenplay.domain.model.ids.TmdbMovieId
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.model.ids.TmdbTvShowId

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
    is TmdbMovieId -> MovieImages(
        backdrops = backdrops,
        posters = posters,
        screenplayId = screenplayId
    )
    is TmdbTvShowId -> TvShowImages(
        backdrops = backdrops,
        posters = posters,
        screenplayId = screenplayId
    )
}

data class MovieImages(
    override val backdrops: List<TmdbBackdropImage>,
    override val posters: List<TmdbPosterImage>,
    override val screenplayId: TmdbMovieId
) : ScreenplayImages

data class TvShowImages(
    override val backdrops: List<TmdbBackdropImage>,
    override val posters: List<TmdbPosterImage>,
    override val screenplayId: TmdbTvShowId
) : ScreenplayImages
