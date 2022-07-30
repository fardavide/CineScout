package cinescout.account.tmdb.domain.model

data class TmdbAccount(
    val username: TmdbAccountUsername
)

@JvmInline
value class TmdbAccountUsername(val value: String)
