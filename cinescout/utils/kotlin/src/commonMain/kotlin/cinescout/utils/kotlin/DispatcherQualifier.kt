package cinescout.utils.kotlin

import org.koin.core.qualifier.named

object DispatcherQualifier {

    val Io = named("Io dispatcher")
}
