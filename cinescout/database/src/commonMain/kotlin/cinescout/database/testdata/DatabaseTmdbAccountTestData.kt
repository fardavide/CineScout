package cinescout.database.testdata

import cinescout.database.model.DatabaseGravatarHash
import cinescout.database.model.DatabaseTmdbAccountUsername

object DatabaseTmdbAccountTestData {

    val GravatarHash = DatabaseGravatarHash("hash")
    val Username = DatabaseTmdbAccountUsername("username")
    val AnotherGravatarHash = DatabaseGravatarHash("Another hash")
    val AnotherUsername = DatabaseTmdbAccountUsername("Another username")
}
