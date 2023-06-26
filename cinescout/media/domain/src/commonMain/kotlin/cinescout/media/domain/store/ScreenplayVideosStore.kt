package cinescout.media.domain.store

import cinescout.media.domain.model.ScreenplayVideos
import cinescout.screenplay.domain.model.id.TmdbScreenplayId
import cinescout.store5.Store5

interface ScreenplayVideosStore : Store5<TmdbScreenplayId, ScreenplayVideos>
