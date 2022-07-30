package cinescout.accuount.tmdb.data.local.mapper

import cinescout.account.tmdb.domain.model.TmdbAccount
import cinescout.account.tmdb.domain.model.TmdbAccountUsername
import cinescout.database.model.DatabaseTmdbAccount

class TmdbAccountMapper {

    fun toTmdbAccount(databaseTmdbAccount: DatabaseTmdbAccount): TmdbAccount =
        TmdbAccount(username = TmdbAccountUsername(databaseTmdbAccount.username.value))
}
