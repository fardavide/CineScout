package cinescout.plugins.util

import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.SourceSetContainer

/**
 * @return the [SourceSetContainer] of this [Project]
 */
val Project.sourceSets: SourceSetContainer get() = extensions.getByType()

/**
 * Invokes Groovy's [ExtensionContainer.configure] reifying the type [T].
 */
inline fun <reified T : Any> ExtensionContainer.configure(noinline action: (T) -> Unit) {
    configure(T::class.java, action)
}

/**
 * Invokes Groovy's [ExtensionContainer.configure] reifying the type [T], if extension is present.
 */
inline fun <reified T : Any> ExtensionContainer.configureIfPresent(noinline action: (T) -> Unit) {
    if (findByType<T>() != null) {
        configure(T::class.java, action)
    }
}

/**
 * Invokes Groovy's [ExtensionContainer.create] reifying the type [T].
 */
inline fun <reified T : Any> ExtensionContainer.create(name: String): T = create(name, T::class.java)

/**
 * Invokes Groovy's [ExtensionContainer.findByType] reifying the type [T].
 */
inline fun <reified T : Any> ExtensionContainer.findByType(): T? = findByType(T::class.java)

/**
 * Invokes Groovy's [ExtensionContainer.getByType] reifying the type [T].
 */
inline fun <reified T : Any> ExtensionContainer.getByType(): T = getByType(T::class.java)

