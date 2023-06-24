package cinescout.sync.domain.usecase

interface ShouldSyncWatchlist {

    suspend operator fun invoke(): Boolean
}
