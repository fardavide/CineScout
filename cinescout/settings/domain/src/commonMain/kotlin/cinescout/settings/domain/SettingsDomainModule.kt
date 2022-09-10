package cinescout.settings.domain

import cinescout.settings.domain.usecase.SetForYouHintShown
import cinescout.settings.domain.usecase.ShouldShowForYouHint
import org.koin.dsl.module

val SettingsDomainModule = module {

    factory { SetForYouHintShown(settingsRepository = get()) }
    factory { ShouldShowForYouHint(settingsRepository = get()) }
}
