package screenplay.data.remote.trakt.res

object TraktExtendedScreenplayJson {

    val BreakingBad = """
    {
      "title": "Breaking Bad",
      "year": 2008,
      "ids": {
        "trakt": 1388,
        "slug": "breaking-bad",
        "tvdb": 81189,
        "imdb": "tt0903747",
        "tmdb": 1396,
        "tvrage": 18164
      },
      "overview": "When Walter White, a New Mexico chemistry teacher, is diagnosed with Stage III cancer and given a prognosis of only two years left to live. He becomes filled with a sense of fearlessness and an unrelenting desire to secure his family's financial future at any cost as he enters the dangerous world of drugs and crime.",
      "first_aired": "2008-01-21T02:00:00.000Z",
      "airs": {
        "day": "Sunday",
        "time": "21:00",
        "timezone": "America/New_York"
      },
      "runtime": 45,
      "certification": "TV-MA",
      "network": "AMC",
      "country": "us",
      "trailer": "https://youtube.com/watch?v=XZ8daibM3AE",
      "homepage": "http://www.amc.com/shows/breaking-bad",
      "status": "ended",
      "rating": 9.2584,
      "votes": 85617,
      "comment_count": 429,
      "updated_at": "2023-04-18T08:42:51.000Z",
      "language": "en",
      "genres": [
        "drama",
        "crime"
      ],
      "aired_episodes": 62
    }
    """.trimIndent()

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

    val TomAndJerry = """
    {
      "title": "Tom and Jerry",
      "year": 1940,
      "ids": {
        "trakt": 3913,
        "slug": "tom-and-jerry",
        "tvdb": 72860,
        "imdb": "tt16311516",
        "tmdb": null,
        "tvrage": null
      },
      "overview": "This is all the Tom and Jerry shorts, from 1940 to 1967. The first 114 are from the Hanna-Barbera era (1940 – 1958), the next 13 are from the Gene Deitch era (1960 – 1962), and the last 34 are from the Chuck Jones era (1963 – 1967).",
      "first_aired": "1940-02-10T05:00:00.000Z",
      "airs": {
        "day": "",
        "time": "",
        "timezone": "America/New_York"
      },
      "runtime": 7,
      "certification": "TV-G",
      "network": "CBS",
      "country": "us",
      "trailer": {},
      "homepage": {},
      "status": "ended",
      "rating": 8.31963,
      "votes": 1095,
      "comment_count": 4,
      "updated_at": "2023-04-11T14:16:02.000Z",
      "language": "en",
      "available_translations": [
        "en",
        "ko"
      ],
      "genres": [
        "animation"
      ],
      "aired_episodes": 161
    }
    """.trimIndent()
}
