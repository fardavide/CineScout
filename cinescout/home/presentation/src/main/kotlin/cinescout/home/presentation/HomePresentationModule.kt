package cinescout.home.presentation

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
class HomePresentationModule

@Factory // Workaround for module not generated
class Empty
