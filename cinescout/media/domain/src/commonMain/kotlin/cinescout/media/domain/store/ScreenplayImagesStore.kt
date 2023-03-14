package cinescout.media.domain.store

import cinescout.media.domain.model.ScreenplayImages
import cinescout.screenplay.domain.model.TmdbScreenplayId
import cinescout.store5.Store5

interface ScreenplayImagesStore : Store5<TmdbScreenplayId, ScreenplayImages>
