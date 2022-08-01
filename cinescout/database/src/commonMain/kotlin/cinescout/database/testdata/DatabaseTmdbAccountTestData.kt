package cinescout.database.testdata

import cinescout.database.model.DatabaseTmdbAccountUsername

object DatabaseTmdbAccountTestData {

    val GravatarHash = DatabaseAccountTestData.GravatarHash
    val Username = DatabaseTmdbAccountUsername("username")
    val AnotherGravatarHash = DatabaseAccountTestData.AnotherGravatarHash
    val AnotherUsername = DatabaseTmdbAccountUsername("Another username")
}
