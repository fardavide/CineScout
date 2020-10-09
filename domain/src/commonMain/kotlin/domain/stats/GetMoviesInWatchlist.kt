package domain.stats

import entities.stats.StatRepository

class GetMoviesInWatchlist(
    private val stats: StatRepository
) {

    suspend operator fun invoke() =
        stats.watchlist()
}
