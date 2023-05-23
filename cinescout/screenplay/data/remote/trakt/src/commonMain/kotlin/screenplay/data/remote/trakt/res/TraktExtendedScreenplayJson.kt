package screenplay.data.remote.trakt.res

object TraktExtendedScreenplayJson {

    val Avatar3 = """
    {
    	"title": "Avatar 3",
    	"year": 2024,
    	"ids": {
    		"trakt": 62544,
    		"slug": "avatar-3-2024",
    		"imdb": "tt1757678",
    		"tmdb": 83533
    	},
    	"tagline": "",
    	"overview": "The third entry in the Avatar franchise.",
    	"released": "2024-12-20",
    	"runtime": 1,
    	"country": "us",
    	"trailer": null,
    	"homepage": "https://www.avatar.com/movies",
    	"status": "post production",
    	"rating": 6.82353,
    	"votes": 51,
    	"comment_count": 2,
    	"updated_at": "2023-04-01T08:02:47.000Z",
    	"language": "en",
    	"available_translations": [
    		"bg",
    		"en",
    		"fr",
    		"he",
    		"ko",
    		"pl",
    		"ru",
    		"uk",
    		"vi",
    		"zh"
    	],
    	"genres": [
    		"action",
    		"adventure",
    		"science-fiction"
    	],
    	"certification": null
    }
    """.trimIndent()

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
      "runtime": 49,
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

    val Grimm = """
    {
    	"title": "Grimm",
    	"year": 2011,
    	"ids": {
    		"trakt": 39185,
    		"slug": "grimm",
    		"tvdb": 248736,
    		"imdb": "tt1830617",
    		"tmdb": 39351,
    		"tvrage": 28352
    	},
    	"overview": "After Portland homicide detective Nick Burkhardt discovers he's descended from an elite line of criminal profilers known as \"Grimms,\" he increasingly finds his responsibilities as a detective at odds with his new responsibilities as a Grimm.",
    	"first_aired": "2011-10-29T00:00:00.000Z",
    	"airs": {
    		"day": "Friday",
    		"time": "20:00",
    		"timezone": "America/New_York"
    	},
    	"runtime": 45,
    	"certification": "TV-14",
    	"network": "NBC",
    	"country": "us",
    	"trailer": "https://youtube.com/watch?v=2rVy3RBJmNo",
    	"homepage": "http://www.nbc.com/grimm/",
    	"status": "ended",
    	"rating": 7.93077,
    	"votes": 9765,
    	"comment_count": 58,
    	"updated_at": "2023-04-08T08:57:32.000Z",
    	"language": "en",
    	"available_translations": [
    		"ar",
    		"bg",
    		"bs",
    		"cs",
    		"da",
    		"de",
    		"el",
    		"en",
    		"es",
    		"fa",
    		"fr",
    		"he",
    		"hr",
    		"hu",
    		"it",
    		"ja",
    		"ka",
    		"ko",
    		"lt",
    		"nl",
    		"no",
    		"pl",
    		"pt",
    		"ro",
    		"ru",
    		"sv",
    		"th",
    		"tr",
    		"uk",
    		"vi",
    		"zh"
    	],
    	"genres": [
    		"drama",
    		"mystery",
    		"fantasy",
    		"science-fiction"
    	],
    	"aired_episodes": 122
    }
    """.trimIndent()

    val Inception = """
    {
        "title": "Inception",
        "year": 2010,
        "ids": {
            "trakt": 16662,
            "slug": "inception-2010",
            "imdb": "tt1375666",
            "tmdb": 27205
        },
        "tagline": "Your mind is the scene of the crime.",
        "overview": "Cobb, a skilled thief who commits corporate espionage by infiltrating the subconscious of his targets is offered a chance to regain his old life as payment for a task considered to be impossible: \"inception\", the implantation of another person's idea into a target's subconscious.",
        "released": "2010-07-16",
        "runtime": 148,
        "country": "us",
        "trailer": "https://youtube.com/watch?v=JE9z-gy4De4",
        "homepage": "https://www.warnerbros.com/movies/inception",
        "status": "released",
        "rating": 8.653541341653666,
        "votes": 64100,
        "comment_count": 152,
        "updated_at": "2023-04-24T08:03:56.000Z",
        "language": "en",
        "available_translations": [
            "en"
        ],
        "genres": [
            "action",
            "adventure",
            "science-fiction"
        ],
        "certification": "PG-13"
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

    val TheWalkingDeadDeadCity = """
    {
    	"title": "The Walking Dead: Dead City",
    	"year": 2023,
    	"ids": {
    		"trakt": 193872,
    		"slug": "the-walking-dead-dead-city",
    		"tvdb": 417549,
    		"imdb": "tt18546730",
    		"tmdb": 194583,
    		"tvrage": null
    	},
    	"overview": "Maggie and Negan travel to a post-apocalyptic Manhattan, long ago cut off from the mainland. The crumbling city is filled with the dead and denizens who have made New York City their own world full of anarchy, danger, beauty, and terror.",
    	"first_aired": "2023-06-18T04:00:00.000Z",
    	"airs": {
    		"day": "Sunday",
    		"time": "00:00",
    		"timezone": "America/New_York"
    	},
    	"runtime": 42,
    	"certification": null,
    	"network": "AMC",
    	"country": "us",
    	"trailer": null,
    	"homepage": null,
    	"status": "in production",
    	"rating": 6.64706,
    	"votes": 17,
    	"comment_count": 6,
    	"updated_at": "2023-04-23T18:09:59.000Z",
    	"language": "en",
    	"available_translations": [
    		"en",
    		"es",
    		"he",
    		"ka",
    		"ko",
    		"pl",
    		"pt",
    		"ru"
    	],
    	"genres": [
    		"action",
    		"adventure",
    		"drama",
    		"fantasy",
    		"science-fiction",
    		"mystery"
    	],
    	"aired_episodes": 0
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

    val War = """
    {
    	"title": "War",
    	"year": 2019,
    	"ids": {
    		"trakt": 432667,
    		"slug": "war-2019",
    		"imdb": "tt7430722",
    		"tmdb": 585268
    	},
    	"tagline": "",
    	"overview": "Khalid, is entrusted with the task of eliminating Kabir, a former soldier turned rogue, as he engages in an epic battle with his mentor who had taught him everything.",
    	"released": "2019-10-02",
    	"runtime": 156,
    	"country": "in",
    	"trailer": null,
    	"homepage": "https://www.yashrajfilms.com/movies/war",
    	"status": "released",
    	"rating": 6.55286,
    	"votes": 454,
    	"comment_count": 3,
    	"updated_at": "2023-04-11T08:03:20.000Z",
    	"language": "hi",
    	"available_translations": [
    		"ar",
    		"bg",
    		"de",
    		"en",
    		"es",
    		"fa",
    		"fr",
    		"hi",
    		"hu",
    		"ja",
    		"ko",
    		"my",
    		"nl",
    		"pl",
    		"pt",
    		"ru",
    		"th",
    		"tr",
    		"uk",
    		"vi",
    		"zh"
    	],
    	"genres": [
    		"action",
    		"thriller",
    		"adventure"
    	],
    	"certification": null
    }
    """.trimIndent()
}
