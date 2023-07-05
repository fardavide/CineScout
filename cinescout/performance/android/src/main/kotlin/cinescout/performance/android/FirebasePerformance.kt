package cinescout.performance.android

import cinescout.perfomance.Performance
import cinescout.perfomance.Trace
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance
import com.google.firebase.perf.ktx.trace
import org.koin.core.annotation.Single

@Single
internal class FirebasePerformance : Performance {

    init {
        Firebase.performance.isPerformanceCollectionEnabled = true
    }

    override suspend fun <T> trace(traceName: String, block: suspend (Trace) -> T): T =
        Firebase.performance.newTrace(traceName).trace { block(FirebaseTraceWrapper(this)) }
}
