package cinescout.test.android

import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class ViewModelTestListener : BeforeSpecListener, AfterSpecListener {

    override suspend fun beforeSpec(spec: Spec) {
        spec.coroutineTestScope = true
        Dispatchers.setMain(StandardTestDispatcher())
    }

    override suspend fun afterSpec(spec: Spec) {
        Dispatchers.resetMain()
    }
}
