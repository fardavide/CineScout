package stats.local.double

import database.stats.Watchlist
import database.stats.WatchlistQueries
import entities.IntId
import io.mockk.every
import io.mockk.mockk

fun mockWatchlistQueries(watchlist: MutableList<IntId>): WatchlistQueries = mockk {

    every { insert(IntId(any())) } answers {
        watchlist += IntId(firstArg())
    }

    every { selectAll().executeAsList() } answers {
        watchlist.mapIndexed { index: Int, intId: IntId -> Watchlist(IntId(index), intId) }
    }

    every { deleteByMovieId(IntId(any())) } answers {
        val intArg = firstArg<Int>()
        watchlist -= IntId(intArg)
    }
}
