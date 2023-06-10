package cinescout.seasons.domain.store

import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.seasons.domain.model.TvShowSeasonsWithEpisodes
import cinescout.store5.Store5

interface TvShowSeasonsWithEpisodesStore : Store5<TvShowIds, TvShowSeasonsWithEpisodes>
