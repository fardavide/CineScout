package entities.model

data class GravatarImage(
    val thumbnailUrl: String,
    val fullImageUrl: String?
) {
    fun bestImageUrl() =
        fullImageUrl ?: thumbnailUrl
}

data class TmdbImageUrl(val baseUrl: String, val path: String) {

    fun get(size: Size): String =
        "${baseUrl.trimEnd('/')}/${size.name.toLowerCase()}/${path.trimStart('/')}"

    enum class Size {
        W92,
        W154,
        W185,
        W342,
        W500,
        W780,
        Original
    }
}
