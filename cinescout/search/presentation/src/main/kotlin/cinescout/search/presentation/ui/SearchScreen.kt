package cinescout.search.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import cinescout.design.AdaptivePreviews
import cinescout.design.TestTag
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.util.NoContentDescription
import cinescout.design.util.collectAsStateLifecycleAware
import cinescout.search.presentation.action.SearchAction
import cinescout.search.presentation.state.SearchState
import cinescout.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen() {
    val viewModel: SearchViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateLifecycleAware()
    SearchScreen(state = state, search = { viewModel.submit(SearchAction.Search(it)) })
}

@Composable
internal fun SearchScreen(state: SearchState, search: (String) -> Unit) {
    Box(modifier = Modifier.testTag(TestTag.Search)) {
        var searchQuery by remember { mutableStateOf(state.query) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dimens.Margin.Small, end = Dimens.Margin.Small, top = Dimens.Margin.Small),
            value = searchQuery,
            onValueChange = { newQuery ->
                searchQuery = newQuery
                search(newQuery)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = NoContentDescription)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { defaultKeyboardAction(ImeAction.Done) }),
            singleLine = true
        )
    }
}

@Composable
@AdaptivePreviews.WithBackground
private fun SearchScreenPreview() {
    CineScoutTheme {
        SearchScreen(
            state = SearchState(query = "Inception"),
            search = {}
        )
    }
}
