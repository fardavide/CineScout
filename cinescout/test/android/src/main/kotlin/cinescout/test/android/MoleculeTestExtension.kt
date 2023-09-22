package cinescout.test.android

import android.os.Looper
import android.os.Trace
import android.util.SparseArray
import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkConstructor
import io.mockk.unmockkStatic

class MoleculeTestExtension : BeforeSpecListener, AfterSpecListener {

    override suspend fun beforeSpec(spec: Spec) {
        mockkStatic(android.os.Trace::class, Looper::class)
        every { Trace.beginSection(any()) } just runs
        every { Trace.endSection() } just runs
        every { Looper.getMainLooper() } returns mockk(relaxed = true)

        mockkConstructor(SparseArray::class)
        every { anyConstructed<SparseArray<Any>>().clear() } just runs
    }

    override suspend fun afterSpec(spec: Spec) {
        unmockkStatic(Trace::class, Looper::class)
        unmockkConstructor(SparseArray::class)
    }
}
