package cinescout.movies.data.local.mapper

import cinescout.common.model.Rating
import cinescout.database.model.DatabaseTmdbGenreId
import cinescout.database.model.DatabaseTmdbKeywordId
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbPersonId
import cinescout.database.model.DatabaseTmdbVideoId
import cinescout.database.model.DatabaseVideoResolution
import cinescout.database.model.DatabaseVideoSite
import cinescout.database.model.DatabaseVideoType
import cinescout.movies.domain.model.TmdbGenreId
import cinescout.movies.domain.model.TmdbKeywordId
import cinescout.movies.domain.model.TmdbMovieId
import cinescout.movies.domain.model.TmdbPersonId
import cinescout.movies.domain.model.TmdbVideo
import cinescout.movies.domain.model.TmdbVideoId

internal fun DatabaseTmdbGenreId.toId() = TmdbGenreId(value)
internal fun DatabaseTmdbKeywordId.toId() = TmdbKeywordId(value)
internal fun DatabaseTmdbMovieId.toId() = TmdbMovieId(value)
internal fun DatabaseTmdbPersonId.toId() = TmdbPersonId(value)
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
    DatabaseVideoType.Teaser -> TmdbVideo.Type.Teaser
    DatabaseVideoType.Trailer -> TmdbVideo.Type.Trailer
}
internal fun Rating.toDatabaseRating() = value
internal fun TmdbGenreId.toDatabaseId() = DatabaseTmdbGenreId(value)
internal fun TmdbKeywordId.toDatabaseId() = DatabaseTmdbKeywordId(value)
internal fun TmdbPersonId.toDatabaseId() = DatabaseTmdbPersonId(value)
internal fun TmdbMovieId.toDatabaseId() = DatabaseTmdbMovieId(value)
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
    TmdbVideo.Type.Teaser -> DatabaseVideoType.Teaser
    TmdbVideo.Type.Trailer -> DatabaseVideoType.Trailer
}
