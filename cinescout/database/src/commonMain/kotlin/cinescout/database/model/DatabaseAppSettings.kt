package cinescout.database.model

typealias DatabaseAppSettings = cinescout.database.AppSettings

val DefaultDatabaseAppSettings = DatabaseAppSettings(
    id = 1,
    anticipatedSuggestionsEnabled = true,
    inAppSuggestionsEnabled = true,
    personalSuggestionsEnabled = true,
    popularSuggestionsEnabled = true,
    recommendedSuggestionsEnabled = true,
    trendingSuggestionsEnabled = true
)
