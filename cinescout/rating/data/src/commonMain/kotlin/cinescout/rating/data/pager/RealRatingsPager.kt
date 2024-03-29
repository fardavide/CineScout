package cinescout.rating.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.lists.domain.ListParams
import cinescout.lists.domain.PagingDefaults
import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.mediator.RatingsRemoteMediatorFactory
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.pager.RatingsPager
import org.koin.core.annotation.Factory

@Factory
internal class RealRatingsPager(
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteMediatorFactory: RatingsRemoteMediatorFactory
) : RatingsPager {

    override fun create(params: ListParams): Pager<Int, ScreenplayWithPersonalRating> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        remoteMediator = remoteMediatorFactory.create(params.type),
        pagingSourceFactory = { localDataSource.findPagedRatings(params) }
    )
}
