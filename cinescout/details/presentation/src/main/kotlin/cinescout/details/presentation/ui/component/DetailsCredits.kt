package cinescout.details.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import cinescout.design.theme.CineScoutTheme
import cinescout.design.theme.Dimens
import cinescout.design.theme.imageBackground
import cinescout.design.ui.FailureImage
import cinescout.design.ui.ImageStack
import cinescout.design.ui.Modal
import cinescout.details.presentation.model.ScreenplayDetailsUiModel
import cinescout.details.presentation.sample.ScreenplayDetailsUiModelSample
import cinescout.details.presentation.ui.ScreenplayDetailsLayout
import cinescout.resources.R.drawable
import cinescout.resources.R.string
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun DetailsCredits(
    mode: ScreenplayDetailsLayout.Mode,
    creditsMembers: ImmutableList<ScreenplayDetailsUiModel.CreditsMember>,
    openCredits: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (mode) {
        ScreenplayDetailsLayout.Mode.Horizontal -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = Dimens.Margin.Small)
        ) {
            items(creditsMembers) { member ->
                CreditsMember(mode = mode, member = member)
            }
        }
        is ScreenplayDetailsLayout.Mode.Vertical -> Row(
            modifier = modifier
                .clip(MaterialTheme.shapes.large)
                .clickable { openCredits() }
                .padding(horizontal = mode.spacing)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ImageStack(
                properties = ImageStack.Properties.mediumImage(),
                imageModels = creditsMembers.take(6).mapNotNull { it.profileImageUrl }.toImmutableList()
            )
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(id = string.details_see_credit)
                )
            }
        }
    }
}

@Composable
private fun CreditsMember(
    mode: ScreenplayDetailsLayout.Mode,
    member: ScreenplayDetailsUiModel.CreditsMember
) {
    when (mode) {
        ScreenplayDetailsLayout.Mode.Horizontal -> Row(
            modifier = Modifier.padding(vertical = Dimens.Margin.XSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CreditsMemberImage(url = member.profileImageUrl)
            Spacer(modifier = Modifier.width(Dimens.Margin.Small))
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
        is ScreenplayDetailsLayout.Mode.Vertical -> Column(
            modifier = Modifier
                .width(Dimens.Image.Medium + Dimens.Margin.Medium * 2)
                .padding(Dimens.Margin.XSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CreditsMemberImage(url = member.profileImageUrl)
            Spacer(modifier = Modifier.height(Dimens.Margin.XSmall))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.name,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.role.orEmpty(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CreditsMemberImage(url: String?) {
    CoilImage(
        modifier = Modifier
            .size(Dimens.Image.Medium)
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
            modifier = Modifier.padding(horizontal = Dimens.Margin.Medium),
            mode = ScreenplayDetailsLayout.Mode.Horizontal,
            creditsMembers = creditsMembers,
            openCredits = {}
        )
    }
}

@Preview
@Composable
private fun DetailsCreditsPreview() {
    val mode = ScreenplayDetailsLayout.Mode.Vertical(spacing = Dimens.Margin.Small)
    val members = ScreenplayDetailsUiModelSample.Inception.creditsMembers
    CineScoutTheme {
        DetailsCredits(mode = mode, creditsMembers = members, openCredits = {})
    }
}
