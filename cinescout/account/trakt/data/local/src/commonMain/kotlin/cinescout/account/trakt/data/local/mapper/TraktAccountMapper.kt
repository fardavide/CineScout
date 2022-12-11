package cinescout.account.trakt.data.local.mapper

import cinescout.account.domain.model.Gravatar
import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.account.trakt.domain.model.TraktAccountUsername
import cinescout.database.model.DatabaseTraktAccount
import org.koin.core.annotation.Factory

@Factory
class TraktAccountMapper {

    fun toTraktAccount(databaseTraktAccount: DatabaseTraktAccount): TraktAccount =
        TraktAccount(
            gravatar = databaseTraktAccount.gravatarHash?.value?.let(::Gravatar),
            username = TraktAccountUsername(databaseTraktAccount.username.value)
        )
}
