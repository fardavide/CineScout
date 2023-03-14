package cinescout.media.domain.model

import cinescout.screenplay.domain.model.TmdbScreenplayId

sealed interface ScreenplayMedia {

    val backdrops: List<TmdbBackdropImage>
    val posters: List<TmdbPosterImage>
    val videos: List<TmdbVideo>

    companion object {

        fun from(
            screenplayId: TmdbScreenplayId,
            backdrops: List<TmdbBackdropImage>,
            posters: List<TmdbPosterImage>,
            videos: List<TmdbVideo>
        ): ScreenplayMedia = when (screenplayId) {
            is TmdbScreenplayId.Movie -> MovieMedia(backdrops, posters, videos)
            is TmdbScreenplayId.TvShow -> TvShowMedia(backdrops, posters, videos)
        }
    }
}

data class MovieMedia(
    override val backdrops: List<TmdbBackdropImage>,
    override val posters: List<TmdbPosterImage>,
    override val videos: List<TmdbVideo>
) : ScreenplayMedia

data class TvShowMedia(
    override val backdrops: List<TmdbBackdropImage>,
    override val posters: List<TmdbPosterImage>,
    override val videos: List<TmdbVideo>
) : ScreenplayMedia
