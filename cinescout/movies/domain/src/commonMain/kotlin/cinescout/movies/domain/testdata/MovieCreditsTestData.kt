package cinescout.movies.domain.testdata

import cinescout.movies.domain.model.MovieCredits

object MovieCreditsTestData {

    val Inception = MovieCredits(
        movieId = TmdbMovieIdTestData.Inception,
        cast = listOf(
            MovieCredits.CastMember(
                character = "Dom Cobb",
                person = PersonTestData.LeonardoDiCaprio
            ),
            MovieCredits.CastMember(
                character = "Cobb",
                person = PersonTestData.JosephGordonLevitt
            )
        ),
        crew = listOf(
            MovieCredits.CrewMember(
                job = "Director",
                person = PersonTestData.ChristopherNolan
            )
        )
    )
}
