package cinescout.settings.presentation.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cinescout.settings.domain.usecase.GetAppSettings
import cinescout.settings.domain.usecase.UpdateAppSettings
import cinescout.settings.presentation.action.SettingsAction
import cinescout.settings.presentation.model.SettingsUiModel
import cinescout.settings.presentation.state.SettingsState
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class SettingsPresenter(
    private val getAppSettings: GetAppSettings,
    private val updateAppSettings: UpdateAppSettings
) {

    @Composable
    fun models(actions: Flow<SettingsAction>): SettingsState {
        val appSettings by getAppSettings().collectAsState()

        LaunchedEffect(Unit) {
            actions.collect { action ->
                when (action) {
                    is SettingsAction.UpdateAnticipated,
                    is SettingsAction.UpdateInAppGenerated,
                    is SettingsAction.UpdatePersonalSuggestions,
                    is SettingsAction.UpdatePopular,
                    is SettingsAction.UpdateRecommended,
                    is SettingsAction.UpdateTrending -> updateAppSettings(action)
                }
            }
        }

        return SettingsState(
            uiModel = SettingsUiModel(
                suggestions = SettingsUiModel.Suggestions(
                    isAnticipatedEnabled = appSettings.isAnticipatedSuggestionsEnabled,
                    isInAppGeneratedEnabled = appSettings.isInAppGeneratedSuggestionsEnabled,
                    isPersonalSuggestionsEnabled = appSettings.isPersonalSuggestionsEnabled,
                    isPopularEnabled = appSettings.isPopularSuggestionsEnabled,
                    isRecommendedEnabled = appSettings.isRecommendedSuggestionsEnabled,
                    isTrendingEnabled = appSettings.isTrendingSuggestionsEnabled
                )
            )
        )
    }

    private suspend fun updateAppSettings(action: SettingsAction) {
        updateAppSettings { appSettings ->
            when (action) {
                is SettingsAction.UpdateAnticipated -> appSettings.copy(isAnticipatedSuggestionsEnabled = action.value)
                is SettingsAction.UpdateInAppGenerated ->
                    appSettings.copy(isInAppGeneratedSuggestionsEnabled = action.value)
                is SettingsAction.UpdatePersonalSuggestions ->
                    appSettings.copy(isPersonalSuggestionsEnabled = action.value)
                is SettingsAction.UpdatePopular -> appSettings.copy(isPopularSuggestionsEnabled = action.value)
                is SettingsAction.UpdateRecommended -> appSettings.copy(isRecommendedSuggestionsEnabled = action.value)
                is SettingsAction.UpdateTrending -> appSettings.copy(isTrendingSuggestionsEnabled = action.value)
            }
        }
    }
}
