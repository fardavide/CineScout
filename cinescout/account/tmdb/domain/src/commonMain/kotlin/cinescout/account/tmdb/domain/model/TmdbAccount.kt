package cinescout.account.tmdb.domain.model

data class TmdbAccount(
    val name: TmdbAccountName
)

@JvmInline
value class TmdbAccountName(val value: String)
