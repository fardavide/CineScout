package cinescout.media.domain.model

data class TmdbBackdropImage(
    val isPrimary: Boolean,
    val path: String
) {

    fun getUrl(size: Size) = "https://image.tmdb.org/t/p/${size.value}/$path"

    enum class Size(val value: String) {
        SMALL("w300"),
        MEDIUM(value = "w780"),
        LARGE(value = "w1280"),
        ORIGINAL(value = "original")
    }
}
