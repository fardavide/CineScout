package cinescout.accuount.tmdb.data.local.mapper

import cinescout.account.domain.model.Gravatar
import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.account.tmdb.domain.model.TmdbAccountUsername
import cinescout.database.model.DatabaseTmdbAccount

class TmdbAccountMapper {

    fun toTmdbAccount(databaseTmdbAccount: DatabaseTmdbAccount): TmdbAccount =
        TmdbAccount(
            gravatar = databaseTmdbAccount.gravatarHash?.value?.let(::Gravatar),
            username = TmdbAccountUsername(databaseTmdbAccount.username.value)
        )
}
