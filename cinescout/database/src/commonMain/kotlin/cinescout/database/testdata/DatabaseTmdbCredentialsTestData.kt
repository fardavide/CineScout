package cinescout.database.testdata

import cinescout.database.model.DatabaseTmdbAccessToken
import cinescout.database.model.DatabaseTmdbAccountId
import cinescout.database.model.DatabaseTmdbRequestToken
import cinescout.database.model.DatabaseTmdbSessionId

object DatabaseTmdbCredentialsTestData {

    val AccessToken = DatabaseTmdbAccessToken("Access Token")
    val AccountId = DatabaseTmdbAccountId("Account Id")
    val AnotherAccessToken = DatabaseTmdbAccessToken("Another Access Token")
    val AnotherAccountId = DatabaseTmdbAccountId("Another Account Id")
    val AnotherSessionId = DatabaseTmdbSessionId("Another Session Id")
    val RequestToken = DatabaseTmdbRequestToken("Request Token")
    val SessionId = DatabaseTmdbSessionId("Session Id")
}
