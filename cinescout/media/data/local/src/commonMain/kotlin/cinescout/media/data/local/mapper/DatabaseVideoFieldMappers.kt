package cinescout.media.data.local.mapper

import cinescout.database.model.DatabaseTmdbVideoId
import cinescout.database.model.DatabaseVideoResolution
import cinescout.database.model.DatabaseVideoSite
import cinescout.database.model.DatabaseVideoType
import cinescout.media.domain.model.TmdbVideo
import cinescout.media.domain.model.TmdbVideoId

internal fun DatabaseTmdbVideoId.toId() = TmdbVideoId(value)
internal fun DatabaseVideoResolution.toVideoResolution() = when (this) {
    DatabaseVideoResolution.SD -> TmdbVideo.Resolution.SD
    DatabaseVideoResolution.FHD -> TmdbVideo.Resolution.FHD
    DatabaseVideoResolution.UHD -> TmdbVideo.Resolution.UHD
}
internal fun DatabaseVideoSite.toVideoSite() = when (this) {
    DatabaseVideoSite.Vimeo -> TmdbVideo.Site.Vimeo
    DatabaseVideoSite.YouTube -> TmdbVideo.Site.YouTube
}
internal fun DatabaseVideoType.toVideoType() = when (this) {
    DatabaseVideoType.BehindTheScenes -> TmdbVideo.Type.BehindTheScenes
    DatabaseVideoType.Bloopers -> TmdbVideo.Type.Bloopers
    DatabaseVideoType.Clip -> TmdbVideo.Type.Clip
    DatabaseVideoType.Featurette -> TmdbVideo.Type.Featurette
    DatabaseVideoType.OpeningCredits -> TmdbVideo.Type.OpeningCredits
    DatabaseVideoType.Teaser -> TmdbVideo.Type.Teaser
    DatabaseVideoType.Trailer -> TmdbVideo.Type.Trailer
}

internal fun TmdbVideoId.toDatabaseId() = DatabaseTmdbVideoId(value)
internal fun TmdbVideo.Resolution.toDatabaseVideoResolution() = when (this) {
    TmdbVideo.Resolution.SD -> DatabaseVideoResolution.SD
    TmdbVideo.Resolution.FHD -> DatabaseVideoResolution.FHD
    TmdbVideo.Resolution.UHD -> DatabaseVideoResolution.UHD
}
internal fun TmdbVideo.Site.toDatabaseVideoSite(): DatabaseVideoSite = when (this) {
    TmdbVideo.Site.Vimeo -> DatabaseVideoSite.Vimeo
    TmdbVideo.Site.YouTube -> DatabaseVideoSite.YouTube
}
internal fun TmdbVideo.Type.toDatabaseVideoType(): DatabaseVideoType = when (this) {
    TmdbVideo.Type.BehindTheScenes -> DatabaseVideoType.BehindTheScenes
    TmdbVideo.Type.Bloopers -> DatabaseVideoType.Bloopers
    TmdbVideo.Type.Clip -> DatabaseVideoType.Clip
    TmdbVideo.Type.Featurette -> DatabaseVideoType.Featurette
    TmdbVideo.Type.OpeningCredits -> DatabaseVideoType.OpeningCredits
    TmdbVideo.Type.Teaser -> DatabaseVideoType.Teaser
    TmdbVideo.Type.Trailer -> DatabaseVideoType.Trailer
}
