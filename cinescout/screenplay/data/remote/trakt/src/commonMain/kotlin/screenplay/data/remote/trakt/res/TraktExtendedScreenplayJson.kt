package screenplay.data.remote.trakt.res

object TraktExtendedScreenplayJson {

    val Dexter = """
    {
      "title": "Dexter",
      "year": 2006,
      "ids": {
        "trakt": 1396,
        "slug": "dexter",
        "tvdb": 79349,
        "imdb": "tt0773262",
        "tmdb": 1405,
        "tvrage": {}
      },
      "overview": "Dexter Morgan, a blood spatter pattern analyst for the Miami Metro Police also leads a secret life as a serial killer, hunting down criminals who have slipped through the cracks of justice.",
      "first_aired": "2006-10-02T01:00:00.000Z",
      "airs": {
        "day": "Sunday",
        "time": "21:00",
        "timezone": "America/New_York"
      },
      "runtime": 55,
      "certification": "TV-MA",
      "country": "us",
      "trailer": "https://youtube.com/watch?v=YQeUmSD1c3g",
      "homepage": "http://www.sho.com/dexter",
      "status": "ended",
      "rating": 8.48351,
      "votes": 36241,
      "comment_count": 198,
      "network": "Showtime",
      "updated_at": "2023-04-17T08:44:57.000Z",
      "language": "en",
      "genres": [
        "drama",
        "mystery",
        "crime"
      ],
      "aired_episodes": 96
    }
    """.trimIndent()

    val Sherlock = """
    {
      "title": "Sherlock",
      "year": 2010,
      "ids": {
        "trakt": 19792,
        "slug": "sherlock",
        "tvdb": 176941,
        "imdb": "tt1475582",
        "tmdb": 19885,
        "tvrage": 23433
      },
      "overview": "A modern update finds the famous sleuth and his doctor partner solving crime in 21st century London.",
      "first_aired": "2010-07-25T20:00:00.000Z",
      "airs": {
        "day": "Sunday",
        "time": "21:00",
        "timezone": "Europe/London"
      },
      "runtime": 90,
      "certification": "TV-14",
      "country": "gb",
      "trailer": "https://youtube.com/watch?v=xK7S9mrFWL4",
      "homepage": "http://www.bbc.co.uk/programmes/b018ttws",
      "status": "ended",
      "rating": 8.94456,
      "votes": 43434,
      "comment_count": 144,
      "network": "BBC One",
      "updated_at": "2023-04-14T08:34:51.000Z",
      "language": "en",
      "genres": [
        "drama",
        "crime",
        "mystery"
      ],
      "aired_episodes": 12
    }
    """.trimIndent()
}
