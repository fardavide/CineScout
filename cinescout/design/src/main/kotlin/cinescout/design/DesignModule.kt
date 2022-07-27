package cinescout.design

import org.koin.dsl.module

val DesignModule = module {
    
    factory { NetworkErrorToMessageMapper() }
}
