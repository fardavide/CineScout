package cinescout.suggestions.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SuggestionsMode {

    /**
     * Generates suggestions after fetching all the known movies ( rated, in watchlist, etc. ).
     *
     * Suggested for a long-running operation while the app is in background
     */
    Deep,

    /**
     * Generates suggestions after fetching a partial data ( i.e. first page of known movies ).
     *
     * Suggested for generates some suggestions quickly, to give to the user as soon as possible
     */
    Quick
}
