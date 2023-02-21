package cinescout.account.tmdb.data.local.mapper

import cinescout.account.domain.model.Account
import cinescout.account.domain.model.AccountUsername
import cinescout.account.domain.model.Gravatar
import cinescout.database.model.DatabaseTmdbAccount
import org.koin.core.annotation.Factory

@Factory
class TmdbAccountMapper {

    fun toTmdbAccount(databaseTmdbAccount: DatabaseTmdbAccount): Account.Tmdb = Account.Tmdb(
        gravatar = databaseTmdbAccount.gravatarHash?.value?.let(::Gravatar),
        username = AccountUsername(databaseTmdbAccount.username.value)
    )
}
