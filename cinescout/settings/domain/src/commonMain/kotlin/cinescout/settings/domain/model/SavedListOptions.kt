package cinescout.settings.domain.model

data class SavedListOptions(
    val filter: Filter,
    val sorting: Sorting,
    val type: Type
) {

    enum class Filter {
        Disliked,
        Liked,
        Rated,
        Watchlist
    }

    enum class Sorting {
        RatingAscending,
        RatingDescending,
        ReleaseDateAscending,
        ReleaseDateDescending
    }

    enum class Type {
        All,
        Movies,
        TvShows
    }
}
