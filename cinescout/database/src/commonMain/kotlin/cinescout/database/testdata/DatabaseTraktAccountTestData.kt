package cinescout.database.testdata

import cinescout.database.model.DatabaseTraktAccountUsername

object DatabaseTraktAccountTestData {

    val GravatarHash = DatabaseAccountTestData.GravatarHash
    val Username = DatabaseTraktAccountUsername("Trakt username")
    val AnotherGravatarHash = DatabaseAccountTestData.AnotherGravatarHash
    val AnotherUsername = DatabaseTraktAccountUsername("Another username")
}
