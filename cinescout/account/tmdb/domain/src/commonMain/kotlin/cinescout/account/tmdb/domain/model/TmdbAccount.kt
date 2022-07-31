package cinescout.account.tmdb.domain.model

data class TmdbAccount(
    val gravatar: Gravatar?,
    val username: TmdbAccountUsername
)

@JvmInline
value class TmdbAccountUsername(val value: String)

@JvmInline
value class Gravatar(val hash: String) {

    fun getUrl(size: Size) = "https://www.gravatar.com/avatar/$hash?s=${size.pixels}.jpg"

    enum class Size(val pixels: Int) {
        SMALL(pixels = 256),
        MEDIUM(pixels = 512),
        LARGE(pixels = 1024),
    }
}
