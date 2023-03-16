package cinescout.fetchdata.data.mapper

import org.koin.core.annotation.Factory

@Factory
internal class FetchDataKeyMapper {

    fun toDatabaseKey(key: Any): String = checkAtomic(key, buildString(key))

    private fun checkAtomic(key: Any, string: String): String {
        require("@" !in string) {
            "String value for the key is not atomic, you may want to use a data class. Key: $key, String: $string"
        }
        return string
    }

    private fun buildString(key: Any) = key.toString().replace(
        oldValue = requireClassName(key, key::class.simpleName),
        newValue = requireClassName(key, key::class.qualifiedName)
    )

    private fun requireClassName(key: Any, className: String?) =
        requireNotNull(className) { "Cannot resolve class name for $key, perhaps you're using a local class" }
}
