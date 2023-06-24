package cinescout.sync.automated.usecase

import cinescout.sync.automated.worker.AutomatedSyncWorker
import org.koin.core.annotation.Factory

interface StartAutomatedSync {

    operator fun invoke()
}

@Factory
internal class WorkerStartAutomatedSync(
    private val scheduler: AutomatedSyncWorker.Scheduler
) : StartAutomatedSync {

    override fun invoke() {
        scheduler()
    }
}
