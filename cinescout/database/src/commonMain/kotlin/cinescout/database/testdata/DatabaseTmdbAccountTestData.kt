package cinescout.database.testdata

import cinescout.database.model.DatabaseTmdbAccountUsername

object DatabaseTmdbAccountTestData {

    val GravatarHash = DatabaseAccountTestData.GravatarHash
    val Username = DatabaseTmdbAccountUsername("Tmdb username")
    val AnotherGravatarHash = DatabaseAccountTestData.AnotherGravatarHash
    val AnotherUsername = DatabaseTmdbAccountUsername("Another username")
}
