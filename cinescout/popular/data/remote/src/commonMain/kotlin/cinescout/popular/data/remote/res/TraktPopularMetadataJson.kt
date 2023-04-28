package cinescout.popular.data.remote.res

import cinescout.screenplay.domain.sample.ScreenplayIdsSample

object TraktPopularMetadataJson {

    val OneMovie = """
      [
        {
          "title": "Avatar 3",
          "year": 2024,
          "ids": {
            "trakt": ${ScreenplayIdsSample.Avatar3.trakt.value},
            "slug": "avatar-3-2024",
            "imdb": "tt1757678",
            "tmdb": ${ScreenplayIdsSample.Avatar3.tmdb.value}
          }
        }
      ]
    """.trimIndent()

    val OneTvShow = """
      [
        {
          "title": "The Walking Dead: Dead City",
          "year": 2023,
          "ids": {
            "trakt": ${ScreenplayIdsSample.TheWalkingDeadDeadCity.trakt.value},
            "slug": "the-walking-dead-dead-city",
            "tvdb": 417549,
            "imdb": "tt18546730",
            "tmdb": ${ScreenplayIdsSample.TheWalkingDeadDeadCity.tmdb.value},
            "tvrage": {}
          }
        }
      ]
    """.trimIndent()
}
