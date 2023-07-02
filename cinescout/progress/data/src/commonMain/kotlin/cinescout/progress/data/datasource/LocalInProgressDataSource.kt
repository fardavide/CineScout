package cinescout.progress.data.datasource

import androidx.paging.PagingSource
import cinescout.lists.domain.ListParams
import cinescout.screenplay.domain.model.Screenplay

interface LocalInProgressDataSource {

    fun findPagedInProgress(params: ListParams): PagingSource<Int, Screenplay>
}
