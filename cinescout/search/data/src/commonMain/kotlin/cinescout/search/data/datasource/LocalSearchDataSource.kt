package cinescout.search.data.datasource

import androidx.paging.PagingSource
import cinescout.screenplay.domain.model.Screenplay
import cinescout.screenplay.domain.model.ScreenplayType

interface LocalSearchDataSource {

    suspend fun insertAll(screenplays: List<Screenplay>)

    fun searchPaged(type: ScreenplayType, query: String): PagingSource<Int, Screenplay>
}
