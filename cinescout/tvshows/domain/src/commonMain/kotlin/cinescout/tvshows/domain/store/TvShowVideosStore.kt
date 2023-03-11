package cinescout.tvshows.domain.store

import cinescout.store5.Store5
import cinescout.tvshows.domain.model.TmdbTvShowId
import cinescout.tvshows.domain.model.TvShowVideos

interface TvShowVideosStore : Store5<TvShowVideosStoreKey, TvShowVideos>

@JvmInline
value class TvShowVideosStoreKey(val tvShowId: TmdbTvShowId)
