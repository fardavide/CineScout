package cinescout.database.testdata

import cinescout.database.model.DatabaseTraktAccessToken
import cinescout.database.model.DatabaseTraktRefreshToken

object DatabaseTraktTokensTestData {

    val AccessToken = DatabaseTraktAccessToken("Access Token")
    val AnotherAccessToken = DatabaseTraktAccessToken("Another Access Token")
    val RefreshToken = DatabaseTraktRefreshToken("Refresh Token")
    val AnotherRefreshToken = DatabaseTraktRefreshToken("Another Refresh Token")
}
