package cinescout.store

import kotlin.test.Test
import kotlin.test.assertEquals

class PagedDataTest {

    @Test
    fun `merge 2 PagedData`() {
        // given
        data class MovieRating(val movieId: Int, val rating: Int)
        val first = pagedDataOf(
            MovieRating(1, 1),
            MovieRating(2, 2),
            MovieRating(3, 3)
        )
        val second = pagedDataOf(
            MovieRating(2, 4),
            MovieRating(3, 5),
            MovieRating(4, 6)
        )
        val expected = pagedDataOf(
            MovieRating(1, 1),
            MovieRating(2, 3),
            MovieRating(3, 4),
            MovieRating(4, 6),
            paging = first.paging + second.paging
        )

        // when
        val result = mergePagedData(
            first = first,
            second = second,
            id = { movieRating -> movieRating.movieId },
            onConflict = { a, b -> a.copy(rating = (a.rating + b.rating) / 2) }
        )

        // then
        assertEquals(expected, result)
    }
}
