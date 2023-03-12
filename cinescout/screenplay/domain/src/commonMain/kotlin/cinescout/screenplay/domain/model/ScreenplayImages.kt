package cinescout.screenplay.domain.model

sealed interface ScreenplayImages {

    val backdrops: List<TmdbBackdropImage>
    val screenplayId: TmdbScreenplayId
    val posters: List<TmdbPosterImage>
}

data class MovieImages(
    override val backdrops: List<TmdbBackdropImage>,
    override val screenplayId: TmdbScreenplayId.Movie,
    override val posters: List<TmdbPosterImage>
) : ScreenplayImages


