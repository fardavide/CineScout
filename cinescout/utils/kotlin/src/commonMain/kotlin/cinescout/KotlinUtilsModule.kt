package cinescout

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named

@Module
@ComponentScan
class KotlinUtilsModule

@Named("app version")
annotation class AppVersionQualifier
