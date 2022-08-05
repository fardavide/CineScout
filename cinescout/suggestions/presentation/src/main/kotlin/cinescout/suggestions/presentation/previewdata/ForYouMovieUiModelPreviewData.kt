package cinescout.suggestions.presentation.previewdata

import cinescout.movies.domain.model.TmdbProfileImage
import cinescout.movies.domain.testdata.MovieCreditsTestData
import cinescout.movies.domain.testdata.MovieTestData
import cinescout.suggestions.presentation.model.ForYouMovieUiModel

object ForYouMovieUiModelPreviewData {

    val Inception = ForYouMovieUiModel(
        tmdbMovieId = MovieTestData.Inception.tmdbId,
        backdropUrl = null, // TODO: MovieTestData.Inception.backdropUrl,
        actors = MovieCreditsTestData.Inception.cast.map { member ->
            val imageUrl = member.person.profileImage.map { image ->
                image.getUrl(TmdbProfileImage.Size.SMALL)
            }
            ForYouMovieUiModel.Actor(imageUrl.orNull().orEmpty())
        },
        posterUrl = "", // TODO: MovieTestData.Inception.posterUrl,
        rating = "idk", // TODO: MovieTestData.Inception.rating.toString(),
        releaseYear = MovieTestData.Inception.releaseDate.year.toString(),
        title = MovieTestData.Inception.title
    )
}
