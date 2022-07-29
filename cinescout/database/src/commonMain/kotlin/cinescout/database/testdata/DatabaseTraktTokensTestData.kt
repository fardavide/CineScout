package cinescout.database.testdata

import cinescout.database.model.DatabaseTraktAccessToken
import cinescout.database.model.DatabaseTraktAuthorizationCode
import cinescout.database.model.DatabaseTraktRefreshToken

object DatabaseTraktTokensTestData {

    val AccessToken = DatabaseTraktAccessToken("Access Token")
    val AnotherAccessToken = DatabaseTraktAccessToken("Another Access Token")
    val AuthorizationCode = DatabaseTraktAuthorizationCode("Authorization Code")
    val AnotherAuthorizationCode = DatabaseTraktAuthorizationCode("Another Authorization Code")
    val RefreshToken = DatabaseTraktRefreshToken("Refresh Token")
    val AnotherRefreshToken = DatabaseTraktRefreshToken("Another Refresh Token")
}
