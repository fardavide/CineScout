package cinescout.settings.data.local

import cinescout.settings.data.LocalSettingsDataSource
import cinescout.utils.kotlin.DispatcherQualifier
import org.koin.dsl.module

val SettingsDataLocalModule = module {
    single<LocalSettingsDataSource> {
        RealLocalSettingsDataSource(
            appScope = get(),
            appSettingsQueries = get(),
            databaseWriteDispatcher = get(DispatcherQualifier.DatabaseWrite),
            ioDispatcher = get(DispatcherQualifier.Io)
        )
    }
}
