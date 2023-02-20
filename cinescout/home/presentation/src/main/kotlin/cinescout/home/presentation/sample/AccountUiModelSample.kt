package cinescout.home.presentation.sample

import cinescout.home.presentation.model.AccountUiModel

object AccountUiModelSample {

    val Tmdb = AccountUiModel(
        imageUrl = null,
        source = AccountUiModel.Source.Tmdb,
        username = "Davide"
    )

    val Trakt = AccountUiModel(
        imageUrl = null,
        source = AccountUiModel.Source.Trakt,
        username = "Davide"
    )
}
