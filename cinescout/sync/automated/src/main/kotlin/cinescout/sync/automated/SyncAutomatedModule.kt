package cinescout.sync.automated

import androidx.work.WorkManager
import cinescout.sync.automated.worker.AutomatedSyncWorker
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class SyncAutomatedModule {

    @Factory
    internal fun automatedSyncWorkerScheduler(workManager: WorkManager) =
        AutomatedSyncWorker.Scheduler(workManager)
}
