package cinescout.settings.data

import cinescout.settings.domain.SettingsRepository
import org.koin.dsl.module

val SettingsDataModule = module {

    factory<SettingsRepository> { RealSettingsRepository(localSettingsDataSource = get()) }
}
