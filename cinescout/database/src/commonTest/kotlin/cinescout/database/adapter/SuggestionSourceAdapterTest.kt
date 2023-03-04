package cinescout.database.adapter

import cinescout.database.model.DatabaseSuggestionSource
import kotlin.test.Test
import kotlin.test.assertEquals

class SuggestionSourceAdapterTest {

    @Test
    fun fromFromLiked() {
        val source = DatabaseSuggestionSource.FromLiked("title")
        val encoded = SuggestionSourceAdapter.encode(source)
        val decoded = SuggestionSourceAdapter.decode(encoded)
        assertEquals(source, decoded)
    }

    @Test
    fun fromFromRated() {
        val source = DatabaseSuggestionSource.FromRated("title", 5)
        val encoded = SuggestionSourceAdapter.encode(source)
        val decoded = SuggestionSourceAdapter.decode(encoded)
        assertEquals(source, decoded)
    }

    @Test
    fun fromFromWatchlist() {
        val source = DatabaseSuggestionSource.FromWatchlist("title")
        val encoded = SuggestionSourceAdapter.encode(source)
        val decoded = SuggestionSourceAdapter.decode(encoded)
        assertEquals(source, decoded)
    }

    @Test
    fun fromPersonalSuggestions() {
        val source = DatabaseSuggestionSource.PersonalSuggestions(
            provider = DatabaseSuggestionSource.PersonalSuggestions.Provider.Tmdb
        )
        val encoded = SuggestionSourceAdapter.encode(source)
        val decoded = SuggestionSourceAdapter.decode(encoded)
        assertEquals(source, decoded)
    }

    @Test
    fun fromPopular() {
        val source = DatabaseSuggestionSource.Popular
        val encoded = SuggestionSourceAdapter.encode(source)
        val decoded = SuggestionSourceAdapter.decode(encoded)
        assertEquals(source, decoded)
    }

    @Test
    fun fromSuggested() {
        val source = DatabaseSuggestionSource.Suggested
        val encoded = SuggestionSourceAdapter.encode(source)
        val decoded = SuggestionSourceAdapter.decode(encoded)
        assertEquals(source, decoded)
    }

    @Test
    fun fromTrending() {
        val source = DatabaseSuggestionSource.Trending
        val encoded = SuggestionSourceAdapter.encode(source)
        val decoded = SuggestionSourceAdapter.decode(encoded)
        assertEquals(source, decoded)
    }

    @Test
    fun fromUpcoming() {
        val source = DatabaseSuggestionSource.Upcoming
        val encoded = SuggestionSourceAdapter.encode(source)
        val decoded = SuggestionSourceAdapter.decode(encoded)
        assertEquals(source, decoded)
    }
}
