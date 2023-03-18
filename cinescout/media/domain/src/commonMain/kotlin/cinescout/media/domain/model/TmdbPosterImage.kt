package cinescout.media.domain.model

data class TmdbPosterImage(
    val isPrimary: Boolean,
    val path: String
) {

    fun getUrl(size: Size) = "https://image.tmdb.org/t/p/${size.value}/$path"

    enum class Size(val value: String) {
        SMALL(value = "w185"),
        MEDIUM(value = "w500"),
        LARGE(value = "w780"),
        ORIGINAL(value = "original")
    }
}
