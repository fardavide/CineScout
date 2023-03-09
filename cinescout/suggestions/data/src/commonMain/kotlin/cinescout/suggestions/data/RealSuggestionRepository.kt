package cinescout.suggestions.data

import arrow.core.Either
import arrow.core.Nel
import cinescout.suggestions.domain.SuggestionRepository
import cinescout.suggestions.domain.model.SuggestedMovie
import cinescout.suggestions.domain.model.SuggestedMovieId
import cinescout.suggestions.domain.model.SuggestedScreenplayId
import cinescout.suggestions.domain.model.SuggestedTvShow
import cinescout.suggestions.domain.model.SuggestedTvShowId
import cinescout.suggestions.domain.model.SuggestionError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
class RealSuggestionRepository(
    private val localSuggestionDataSource: LocalSuggestionDataSource
) : SuggestionRepository {

    override fun getSuggestedMovieIds(): Flow<Either<SuggestionError, Nel<SuggestedMovieId>>> =
        localSuggestionDataSource.findAllSuggestedMovieIds().map { either ->
            either.mapLeft { SuggestionError.NoSuggestions }
        }

    override fun getSuggestedTvShowIds(): Flow<Either<SuggestionError, Nel<SuggestedTvShowId>>> =
        localSuggestionDataSource.findAllSuggestedTvShowIds().map { either ->
            either.mapLeft { SuggestionError.NoSuggestions }
        }

    override suspend fun storeSuggestionIds(screenplays: Collection<SuggestedScreenplayId>) {
        localSuggestionDataSource.insertSuggestionIds(screenplays)
    }

    override suspend fun storeSuggestedMovies(movies: Collection<SuggestedMovie>) {
        localSuggestionDataSource.insertSuggestedMovies(movies)
    }

    override suspend fun storeSuggestedTvShows(tvShows: Collection<SuggestedTvShow>) {
        localSuggestionDataSource.insertSuggestedTvShows(tvShows)
    }
}
