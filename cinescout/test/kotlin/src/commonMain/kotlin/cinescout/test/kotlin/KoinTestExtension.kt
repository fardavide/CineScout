package cinescout.test.kotlin

import io.kotest.core.listeners.AfterSpecListener
import io.kotest.core.listeners.BeforeSpecListener
import io.kotest.core.spec.Spec
import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.KoinAppDeclaration

class KoinTestExtension private constructor(private val appDeclaration: KoinAppDeclaration) :
    BeforeSpecListener, AfterSpecListener {

    lateinit var koin: Koin

    override suspend fun beforeSpec(spec: Spec) {
        koin = startKoin(appDeclaration = appDeclaration).koin
    }

    override suspend fun afterSpec(spec: Spec) {
        stopKoin()
    }

    companion object {

        fun create(appDeclaration: KoinAppDeclaration) = KoinTestExtension(appDeclaration)
    }
}
