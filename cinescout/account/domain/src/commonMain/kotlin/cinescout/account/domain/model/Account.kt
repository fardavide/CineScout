package cinescout.account.domain.model

sealed interface Account {

    val gravatar: Gravatar?
    val username: AccountUsername

    data class Tmdb(
        override val gravatar: Gravatar?,
        override val username: AccountUsername
    ) : Account

    data class Trakt(
        override val gravatar: Gravatar?,
        override val username: AccountUsername
    ) : Account
}
