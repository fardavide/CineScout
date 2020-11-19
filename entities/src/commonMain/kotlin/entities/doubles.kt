package entities

import entities.model.GravatarImage
import entities.model.Name
import entities.model.Profile

object TestData {

    val DummyProfile = Profile(
        id = TmdbId(1),
        name = Name("Davide"),
        username = Name("davide"),
        avatar = GravatarImage("thumb1", "full1"),
        adult = true
    )
}
