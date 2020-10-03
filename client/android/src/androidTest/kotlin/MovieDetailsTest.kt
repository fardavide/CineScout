import androidx.ui.test.assert
import androidx.ui.test.hasText
import androidx.ui.test.onNodeWithTag
import androidx.ui.test.performClick
import client.android.theme.CineScoutTheme
import client.android.ui.MovieDetails
import client.android.ui.TitleTopBarTestTag
import client.android.ui.WatchlistButtonTestTag
import client.awaitData
import client.nextData
import client.viewModel.MovieDetailsViewModel
import domain.Test.Movie.TheBookOfEli
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.*

class MovieDetailsTest : ComposeTest(), KoinTest {

    private lateinit var viewModel: MovieDetailsViewModel

    override fun TestScope.init() {
        setContent {
            CineScoutTheme {
                MovieDetails(
                    buildViewModel = { scope, id ->
                        viewModel = get { parametersOf(scope, id) }
                        viewModel
                    },
                    movieId = TheBookOfEli.id,
                    onBack = {}
                )
            }
        }
    }

    @Test
    fun title_is_shown_correctly() = composeTest {
        viewModel.result.awaitData()
        onNodeWithTag(TitleTopBarTestTag).assert(hasText(TheBookOfEli.name.s))
    }

    @Test
    fun watchlist_work_correctly() = composeTest {
        assertEquals(false, viewModel.result.awaitData().inWatchlist)
        onNodeWithTag(WatchlistButtonTestTag)
            .performClick()
        assertEquals(true, viewModel.result.nextData().inWatchlist)
    }
}
