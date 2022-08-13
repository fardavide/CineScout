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
    val inputString = inputData.getString(InputKey)
        ?: return null
    return Json.decodeFromString(inputString)
}

inline fun <reified T> CoroutineWorker.requireInput(): T =
    requireNotNull(getInput<T>()) { "Input is null" }

inline fun <reified T> CoroutineWorker.createOutput(output: T): Data =
    workDataOf(OutputKey to Json.encodeToString(output))

fun CoroutineWorker.createOutput(output: String): Data =
    workDataOf(OutputKey to output)

inline fun <reified T, B : WorkRequest.Builder<*, *>?, W : WorkRequest?> WorkRequest.Builder<B, W>.setInput(
    input: T
): WorkRequest.Builder<B, W> =
    apply {
        val inputString = Json.encodeToString(input)
        setInputData(workDataOf(InputKey to inputString))
    }

@PublishedApi
internal const val InputKey = "WorkerInput"

@PublishedApi
internal const val OutputKey = "WorkerOutput"
