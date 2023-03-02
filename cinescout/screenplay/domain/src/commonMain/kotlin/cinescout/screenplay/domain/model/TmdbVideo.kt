package cinescout.screenplay.domain.model

data class TmdbVideo(
    val id: TmdbVideoId,
    val key: String,
    val resolution: Resolution,
    val site: Site,
    val title: String,
    val type: Type
) {

    fun getPreviewUrl(): String = when (site) {
        Site.Vimeo -> "https://vimeo.com/$key"
        Site.YouTube -> "https://img.youtube.com/vi/$key/0.jpg"
    }
    fun getVideoUrl(): String = when (site) {
        Site.Vimeo -> "https://player.vimeo.com/video/$key"
        Site.YouTube -> "https://www.youtube.com/watch?v=$key"
    }

    enum class Resolution {
        SD, FHD, UHD
    }

    enum class Site {
        Vimeo, YouTube
    }

    enum class Type {
        BehindTheScenes, Bloopers, Clip, Featurette, OpeningCredits, Teaser, Trailer
    }
}
