package cinescout.account.trakt.data.local.mapper

import cinescout.account.domain.model.Account
import cinescout.account.domain.model.AccountUsername
import cinescout.account.domain.model.Gravatar
import cinescout.database.model.DatabaseTraktAccount
import org.koin.core.annotation.Factory

@Factory
class TraktAccountMapper {

    fun toTraktAccount(databaseTraktAccount: DatabaseTraktAccount): Account = Account(
        gravatar = databaseTraktAccount.gravatarHash?.value?.let(::Gravatar),
        username = AccountUsername(databaseTraktAccount.username.value)
    )
}
