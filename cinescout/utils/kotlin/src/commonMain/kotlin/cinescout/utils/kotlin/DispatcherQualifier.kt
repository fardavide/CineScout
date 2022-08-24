package cinescout.utils.kotlin

import org.koin.core.qualifier.named

object DispatcherQualifier {

    val DatabaseWrite = named("Database write dispatcher")
    val Io = named("Io dispatcher")
}
