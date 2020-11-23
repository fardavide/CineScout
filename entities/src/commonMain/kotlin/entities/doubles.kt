package entities

import entities.model.GravatarImage
import entities.model.Name
import entities.model.TmdbProfile
import entities.model.TraktProfile

object TestData {

    val DummyTmdbProfile = TmdbProfile(
        id = TmdbId(1),
        name = Name("Davide"),
        username = Name("davide"),
        avatar = GravatarImage("thumb1", "full1"),
        adult = true
    )

    val DummyTraktProfile = TraktProfile(
        name = Name("Davide"),
        username = Name("davide"),
        avatar = GravatarImage("thumb1", "full1"),
    )
}
