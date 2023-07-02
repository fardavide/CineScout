package cinescout.sync.automated.usecase

import cinescout.sync.automated.worker.AutomatedSyncWorker
import cinescout.sync.domain.usecase.StartAutomatedSync
import org.koin.core.annotation.Factory

@Factory
internal class WorkerStartAutomatedSync(
    private val scheduler: AutomatedSyncWorker.Scheduler
) : StartAutomatedSync {

    override fun invoke() {
        scheduler()
    }
}
