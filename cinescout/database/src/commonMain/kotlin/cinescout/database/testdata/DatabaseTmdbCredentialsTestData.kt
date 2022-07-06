package cinescout.database.testdata

import cinescout.database.model.DatabaseMovie
import cinescout.database.model.DatabaseTmdbAccessToken
import cinescout.database.model.DatabaseTmdbMovieId
import cinescout.database.model.DatabaseTmdbSessionId

object DatabaseTmdbCredentialsTestData {

    val AccessToken = DatabaseTmdbAccessToken("Access Token")
    val AnotherAccessToken = DatabaseTmdbAccessToken("Another Access Token")
    val AnotherSessionId = DatabaseTmdbSessionId("Another Session Id")
    val SessionId = DatabaseTmdbSessionId("Session Id")
}
