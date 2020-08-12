import movies.MovieRepository

class SearchMovies(
    private val movies: MovieRepository
) {

    suspend operator fun invoke(query: String) =
        movies.search(query)
}
