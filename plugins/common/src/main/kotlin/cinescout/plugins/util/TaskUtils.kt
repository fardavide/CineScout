package cinescout.plugins.util

import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

/**
 * Invokes Groovy's [TaskContainer.register] reifying the type [T].
 */
inline fun <reified T : Task> TaskContainer.register(name: String, noinline block: (T) -> Unit) {
    register(name, T::class.java, block)
}
