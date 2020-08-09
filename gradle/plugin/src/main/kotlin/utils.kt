import studio.forface.easygradle.internal.useIfNotNull

fun String?.module() = useIfNotNull { "-$it" }
