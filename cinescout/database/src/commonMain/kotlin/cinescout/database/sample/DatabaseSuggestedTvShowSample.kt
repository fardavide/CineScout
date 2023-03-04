package cinescout.database.sample

import cinescout.database.model.DatabaseSuggestedTvShow
import cinescout.database.model.DatabaseSuggestionSource

object DatabaseSuggestedTvShowSample {

    val Grimm = DatabaseSuggestedTvShow(
        tmdbId = DatabaseTvShowSample.Grimm.tmdbId,
        affinity = 90.0,
        source = DatabaseSuggestionSource.FromRated(title = "", 9)
    )
}
