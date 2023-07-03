package cinescout.sync.automated.usecase

import cinescout.sync.automated.worker.AutomatedSyncWorker
import cinescout.sync.domain.usecase.ScheduleAutomatedSync
import org.koin.core.annotation.Factory

@Factory
internal class WorkerScheduleAutomatedSync(
    private val scheduler: AutomatedSyncWorker.Scheduler
) : ScheduleAutomatedSync {

    override fun invoke() {
        scheduler.schedulePeriodic()
    }
}
