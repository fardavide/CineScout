package cinescout.rating.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.mediator.RatingsRemoteMediatorFactory
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.pager.RatingsPager
import cinescout.screenplay.domain.model.ScreenplayType
import org.koin.core.annotation.Factory

@Factory
internal class RealRatingsPager(
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteMediatorFactory: RatingsRemoteMediatorFactory
) : RatingsPager {

    override fun create(listType: ScreenplayType): Pager<Int, ScreenplayWithPersonalRating> = Pager(
        config = PagingConfig(pageSize = 50),
        remoteMediator = remoteMediatorFactory.create(listType),
        pagingSourceFactory = { localDataSource.findPagedRatings(listType) }
    )
}