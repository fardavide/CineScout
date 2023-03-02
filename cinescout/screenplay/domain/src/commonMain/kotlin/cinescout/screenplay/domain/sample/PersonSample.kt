package cinescout.screenplay.domain.sample

import arrow.core.none
import arrow.core.some
import cinescout.screenplay.domain.model.Person
import cinescout.screenplay.domain.model.TmdbPersonId
import cinescout.screenplay.domain.model.TmdbProfileImage

object PersonSample {

    val AaronPaul = Person(
        name = "Aaron Paul",
        profileImage = TmdbProfileImage("/lowE44ffgu4UmnOT3wOTKYhtUzp.jpg").some(),
        tmdbId = TmdbPersonId(84_497)
    )

    val AnnaGunn = Person(
        name = "Anna Gunn",
        profileImage = TmdbProfileImage("/adppyeu1a4REN3khtgmXusrapFi.jpg").some(),
        tmdbId = TmdbPersonId(134_531)
    )

    val BryanCranston = Person(
        name = "Bryan Cranston",
        profileImage = TmdbProfileImage("/7Jahy5LZX2Fo8fGJltMreAI49hC.jpg").some(),
        tmdbId = TmdbPersonId(17_419)
    )

    val ChristopherNolan = Person(
        name = "Christopher Nolan",
        profileImage = TmdbProfileImage(path = "/xuAIuYSmsUzKlUMBFGVZaWsY3DZ.jpg").some(),
        tmdbId = TmdbPersonId(value = 525)
    )

    val CillianMurphy = Person(
        name = "Cillian Murphy",
        profileImage = TmdbProfileImage(path = "/i8dOTC0w6V274ev5iAAvo4Ahhpr.jpg").some(),
        tmdbId = TmdbPersonId(value = 2_037)
    )

    val DavidGiuntoli = Person(
        name = "David Giuntoli",
        profileImage = TmdbProfileImage(path = "/xxtB8TfKLMw9Utx5v2b3Z0nu5VL.jpg").some(),
        tmdbId = TmdbPersonId(value = 206_757)
    )

    val DavidGreenwalt = Person(
        name = "David Greenwalt",
        profileImage = none(),
        tmdbId = TmdbPersonId(value = 87_590)
    )

    val DavidZayas = Person(
        name = "David Zayas",
        profileImage = TmdbProfileImage(path = "/bkPsugnYEN9Wa3VsGf6tU6SzRf1.jpg").some(),
        tmdbId = TmdbPersonId(value = 22_821)
    )

    val DileepRao = Person(
        name = "Dileep Rao",
        profileImage = TmdbProfileImage(path = "/k4OiYIhr886ollgOrvrBs2ZIZbc.jpg").some(),
        tmdbId = TmdbPersonId(value = 95_697)
    )

    val ElliotPage = Person(
        name = "Elliot Page",
        profileImage = TmdbProfileImage(path = "/eCeFgzS8dYHnMfWQT0oQitCrsSz.jpg").some(),
        tmdbId = TmdbPersonId(value = 27_578)
    )

    val HrithikRoshan = Person(
        name = "Hrithik Roshan",
        profileImage = TmdbProfileImage(path = "/upKrdABAMK7jZevWAoPYI24iKlR.jpg").some(),
        tmdbId = TmdbPersonId(value = 78_749)
    )

    val JenniferCarpenter = Person(
        name = "Jennifer Carpenter",
        profileImage = TmdbProfileImage(path = "/ooIo2LPHxMxkfjz05UAJsUfbtEy.jpg").some(),
        tmdbId = TmdbPersonId(value = 53_828)
    )

    val JonahHill = Person(
        name = "Jonah Hill",
        profileImage = TmdbProfileImage(path = "/1IzQOtqYtU3uYzqcjvzQYtU3uYzqcjvzQ.jpg").some(),
        tmdbId = TmdbPersonId(value = 21_007)
    )

    val JosephGordonLevitt = Person(
        name = "Joseph Gordon-Levitt",
        profileImage = TmdbProfileImage(path = "/dhv9f3AaozOjpvjAwVzOWlmmT2V.jpg").some(),
        tmdbId = TmdbPersonId(value = 24_045)
    )

    val KenWatanabe = Person(
        name = "Ken Watanabe",
        profileImage = TmdbProfileImage(path = "/psAXOYp9SBOXvg6AXzARDedNQ9P.jpg").some(),
        tmdbId = TmdbPersonId(value = 3_899)
    )

    val LeonardoDiCaprio = Person(
        name = "Leonardo DiCaprio",
        profileImage = TmdbProfileImage(path = "/wo2hJpn04vbtmh0B9utCFdsQhxM.jpg").some(),
        tmdbId = TmdbPersonId(value = 6_193)
    )

    val MartinScorsese = Person(
        name = "Martin Scorsese",
        profileImage = TmdbProfileImage(path = "/q9QgL0WKcQZwqQZQXQXx8ZyqJZQQZQ.jpg").some(),
        tmdbId = TmdbPersonId(value = 1_032)
    )

    val MichaelCHall = Person(
        name = "Michael C. Hall",
        profileImage = TmdbProfileImage(path = "/7zUMGoujuev5PUwwv4Gl6ikB50k.jpg").some(),
        tmdbId = TmdbPersonId(value = 53_820)
    )

    val RussellHornsby = Person(
        name = "Russell Hornsby",
        profileImage = TmdbProfileImage(path = "/lA2XCK2GgwVoboX3NskkKp5rxOF.jpg").some(),
        tmdbId = TmdbPersonId(value = 62_649)
    )

    val SilasWeirMitchell = Person(
        name = "Silas Weir Mitchell",
        profileImage = TmdbProfileImage(path = "/6TDZz2GdGDorb7ZGDpotMn27cOm.jpg").some(),
        tmdbId = TmdbPersonId(value = 49_815)
    )

    val SimoneBar = Person(
        name = "Simone Bär",
        profileImage = TmdbProfileImage(path = "/Z1sJ73Rcq8VzUpJzJLPDjiAnAp.jpg").some(),
        tmdbId = TmdbPersonId(value = 4_222)
    )

    val TigerShroff = Person(
        name = "Tiger Shroff",
        profileImage = TmdbProfileImage(path = "/kgvll1RBCcDTs6KJZFDb291AuZb.jpg").some(),
        tmdbId = TmdbPersonId(value = 1_338_512)
    )

    val TomHardy = Person(
        name = "Tom Hardy",
        profileImage = TmdbProfileImage(path = "/yVGF9FvDxTDPhGimTbZNfghpllA.jpg").some(),
        tmdbId = TmdbPersonId(value = 2_524)
    )

    val VinceGilligan = Person(
        name = "Vince Gilligan",
        profileImage = TmdbProfileImage(path = "/uFh3OrBvkwKSU3N5y0XnXOhqBJz.jpg").some(),
        tmdbId = TmdbPersonId(value = 66_633)
    )
}
