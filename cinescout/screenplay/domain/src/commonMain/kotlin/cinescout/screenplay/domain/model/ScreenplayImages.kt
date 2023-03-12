package cinescout.screenplay.domain.model

sealed interface ScreenplayImages {

    val backdrops: List<TmdbBackdropImage>
    val posters: List<TmdbPosterImage>
    val screenplayId: TmdbScreenplayId
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
