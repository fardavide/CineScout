package cinescout.test.android

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

class PagingTestExtension(
    private val mainDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : BeforeSpecListener, AfterSpecListener {

    @SuppressLint("WrongConstant")
    override suspend fun beforeSpec(spec: Spec) {
        mockkStatic(Log::class, Looper::class)
        every { Log.isLoggable(any(), any()) } returns false
        every { Looper.getMainLooper() } returns mockk(relaxed = true)

        Dispatchers.setMain(mainDispatcher)
    }

    override suspend fun afterSpec(spec: Spec) {
        unmockkStatic(Log::class, Looper::class)
    }
}
