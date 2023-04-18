@file:Suppress("UnusedReceiverParameter")

package cinescout.utils.android

import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkRequest
import androidx.work.workDataOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> CoroutineWorker.getInput(): T? {
    val inputString = inputData.getString(WorkerInputKey)
        ?: return null
    return Json.decodeFromString(inputString)
}

inline fun <reified T> CoroutineWorker.requireInput(): T = requireNotNull(getInput<T>()) {
    "Input is null"
}

inline fun <reified T> CoroutineWorker.createOutput(output: T): Data =
    workDataOf(WorkerOutputKey to Json.encodeToString(output))

fun CoroutineWorker.createOutput(output: String): Data = workDataOf(WorkerOutputKey to output)

inline fun <reified T, B : WorkRequest.Builder<B, *>, W : WorkRequest> WorkRequest.Builder<B, W>.setInput(
    input: T
): WorkRequest.Builder<B, W> = apply {
    val inputString = Json.encodeToString(input)
    setInputData(workDataOf(WorkerInputKey to inputString))
}

const val WorkerInputKey = "WorkerInput"

@PublishedApi
internal const val WorkerOutputKey = "WorkerOutput"
