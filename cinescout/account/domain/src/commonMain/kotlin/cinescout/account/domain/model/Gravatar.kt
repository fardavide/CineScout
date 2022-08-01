package cinescout.account.domain.model

@JvmInline
value class Gravatar(val hash: String) {

    fun getUrl(size: Size) = "https://www.gravatar.com/avatar/$hash?s=${size.pixels}.jpg"

    enum class Size(val pixels: Int) {
        SMALL(pixels = 256),
        MEDIUM(pixels = 512),
        LARGE(pixels = 1024),
    }

    companion object {

        fun fromUrl(url: String) = Gravatar(url.substringAfterLast("/").substringBefore("?"))
    }
}
