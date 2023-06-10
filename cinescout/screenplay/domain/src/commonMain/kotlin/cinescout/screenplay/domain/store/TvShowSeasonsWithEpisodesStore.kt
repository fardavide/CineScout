package cinescout.screenplay.domain.store

import cinescout.screenplay.domain.model.TvShowSeasonsWithEpisodes
import cinescout.screenplay.domain.model.ids.TvShowIds
import cinescout.store5.Store5

interface TvShowSeasonsWithEpisodesStore : Store5<TvShowIds, TvShowSeasonsWithEpisodes>
