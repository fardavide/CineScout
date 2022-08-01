package cinescout.account.trakt.domain.testData

import cinescout.account.trakt.domain.model.TraktAccount
import cinescout.account.trakt.domain.model.TraktAccountUsername

object TraktAccountTestData {

    val Username = TraktAccountUsername("Trakt")
    val Account = TraktAccount(username = Username)
}
