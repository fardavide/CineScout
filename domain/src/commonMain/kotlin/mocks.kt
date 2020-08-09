import movies.MovieRepository
import stats.StatRepository

import org.koin.dsl.module

val domainMockModule = module {
    factory<MovieRepository> { MockMovieRepository() }
    factory<StatRepository> { MockStatRepository() }
}

// @VisibleForTesting
internal class MockMovieRepository : MovieRepository

// @VisibleForTesting
internal class MockStatRepository : StatRepository
