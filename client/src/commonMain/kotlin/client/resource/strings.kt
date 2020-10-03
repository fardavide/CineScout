package client.resource

// TODO use intl
val Strings = object : StringResources() {}

abstract class StringResources {

    val AppName = "CineScout"

    // Actions
    val DislikeAction = "Dislike"
    val GoToSearchAction = "Go to search"
    val GoToSuggestionsAction = "Go to suggestions"
    val LikeAction = "Like"
    val SearchAction = "Search"
    val RateMovieAction = "Rate movie"
    val SkipAction = "Skip"
    val SuggestionsAction = "Suggestions"
    val WatchlistAction = "Watchlist"

    // Errors
    val GenericError = "An error has occurred"
    val NoRateMoviesError = "You haven't rate any movie"
    val NoWatchlistMoviesError = "Your watchlist is empty"

    // Hints
    val InsertMovieTitleHint = "Insert the Movie title"

    // Messages
    val GetSuggestionsAndAddToWatchlist = "Get some suggestions and add some movies to watchlist"
    val LoadingMessage = "Loading..."
    val SearchMovieAndRateForSuggestions = "Search a movie you like and rate it for generate your first suggestion"

    // Prompts
    val RateMoviePrompt = "Do you like this movie?"

    // Titles
    val ActorsTitle = "Actors"
    val GenresTitle = "Genres"
}
