package cinescout.suggestions.domain.model

enum class SuggestionsMode {

    /**
     * Generates suggestions after fetching all the known movies ( rated, in watchlist, etc. ) and calculates the
     *  affinity for the user.
     *
     * Suggested for a long-running operation while the app is in background
     */
    Deep,

    /**
     * Generates suggestions after fetching a partial data ( i.e. first page of known movies ) and does not calculate
     *  affinity for the user.
     *
     * Suggested for generates some suggestions quickly, to give to the user as soon as possible
     */
    Quick
}
