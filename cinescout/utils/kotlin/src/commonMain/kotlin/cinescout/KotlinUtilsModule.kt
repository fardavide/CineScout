package cinescout

import org.koin.core.qualifier.named
import org.koin.dsl.module

val KotlinUtilsModule = module {

    factory { GetAppVersion(appVersion = get(AppVersionQualifier)) }
}

val AppVersionQualifier = named("App version")
