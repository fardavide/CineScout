package cinescout.details.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.FailureImage
import cinescout.design.ui.ImageStack
import cinescout.design.ui.Modal
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.details.presentation.ui.ScreenplayDetailsScreen
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import cinescout.resources.TextRes
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun DetailsCredits(
    mode: DetailsCredits.Mode,
    creditsMembers: ImmutableList<ScreenplayDetailsUiModel.CreditsMember>,
    openCredits: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (mode) {
        DetailsCredits.Mode.VerticalList -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = Dimens.Margin.small)
        ) {
            items(creditsMembers) { member ->
                CreditsMember(member = member)
            }
        }
        is DetailsCredits.Mode.HorizontalStack -> DetailsNavigableRow(
            modifier = modifier,
            onClick = openCredits,
            contentDescription = TextRes(string.details_see_credit)
        ) {
            ImageStack(
                properties = ImageStack.Properties.mediumImage(),
                imageModels = creditsMembers.take(6).mapNotNull { it.profileImageUrl }.toImmutableList()
            )
        }
    }
}

@Composable
private fun CreditsMember(member: ScreenplayDetailsUiModel.CreditsMember) {
    Row(
        modifier = Modifier.padding(vertical = Dimens.Margin.xSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CreditsMemberImage(url = member.profileImageUrl)
        Spacer(modifier = Modifier.width(Dimens.Margin.small))
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.name,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.role.orEmpty(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CreditsMemberImage(url: String?) {
    CoilImage(
        modifier = Modifier
            .size(Dimens.Image.medium)
            .clip(CircleShape)
            .imageBackground(),
        imageModel = { url },
        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
        failure = { FailureImage() },
        previewPlaceholder = drawable.ic_user_color
    )
}

@Composable
internal fun DetailsCreditsModal(
    creditsMembers: ImmutableList<ScreenplayDetailsUiModel.CreditsMember>,
    onDismiss: () -> Unit
) {
    Modal(onDismiss = onDismiss) {
        DetailsCredits(
            modifier = Modifier.padding(horizontal = Dimens.Margin.medium),
            mode = DetailsCredits.Mode.VerticalList,
            creditsMembers = creditsMembers,
            openCredits = {}
        )
    }
}

object DetailsCredits {

    sealed interface Mode {

        object VerticalList : Mode

        @JvmInline
        value class HorizontalStack(val spacing: Dp) : Mode

        companion object {

            fun from(screenMode: ScreenplayDetailsScreen.Mode) = when (screenMode) {
                is ScreenplayDetailsScreen.Mode.OnePane -> HorizontalStack(spacing = screenMode.spacing)
                ScreenplayDetailsScreen.Mode.TwoPane -> HorizontalStack(spacing = Dimens.Margin.small)
                ScreenplayDetailsScreen.Mode.ThreePane -> VerticalList
            }
        }
    }
}

@Preview
@Composable
private fun HorizontalStackDetailsCreditsPreview() {
    val mode = DetailsCredits.Mode.HorizontalStack(spacing = Dimens.Margin.small)
    val members = ScreenplayDetailsUiModelSample.Inception.creditsMembers
    CineScoutTheme {
        DetailsCredits(mode = mode, creditsMembers = members, openCredits = {})
    }
}

@Preview
@Composable
private fun VerticalListDetailsCreditsPreview() {
    val mode = DetailsCredits.Mode.VerticalList
    val members = ScreenplayDetailsUiModelSample.Inception.creditsMembers
    CineScoutTheme {
        DetailsCredits(mode = mode, creditsMembers = members, openCredits = {})
    }
}
