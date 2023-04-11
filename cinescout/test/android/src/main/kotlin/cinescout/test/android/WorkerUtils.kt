package cinescout.test.android

import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import cinescout.utils.android.WorkerInputKey
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T, W : ListenableWorker> TestListenableWorkerBuilder<W>.setInput(
    input: T
): TestListenableWorkerBuilder<W> = apply {
    val inputString = Json.encodeToString(input)
    setInputData(workDataOf(WorkerInputKey to inputString))
}
