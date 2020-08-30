package client.resource

// TODO use intl
val Strings = object : StringResources() {}

abstract class StringResources {

    val AppName = "CineScout"

    // Actions
    val GoToSearchAction = "Go to search"
    val SearchAction = "Search"
    val RateMovieAction = "Rate movie"
    val SuggestionsAction = "Suggestions"

    // Errors
    val GenericError = "An error has occurred"
    val NoRateMoviesError = "You haven't rate any movie"

    // Hints

    // Messages
    val LoadingMessage = "Loading..."
    val SearchMovieAndRateForSuggestions = "Search a movie you like and rate it for generate your first suggestion"

    // Titles
    val ActorsTitle = "Actors"
    val GenresTitle = "Genres"
}
