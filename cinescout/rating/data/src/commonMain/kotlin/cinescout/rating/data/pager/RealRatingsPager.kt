package cinescout.rating.data.pager

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import arrow.core.Option
import cinescout.lists.domain.ListSorting
import cinescout.lists.domain.PagingDefaults
import cinescout.rating.data.datasource.LocalPersonalRatingDataSource
import cinescout.rating.data.mediator.RatingsRemoteMediatorFactory
import cinescout.rating.domain.model.ScreenplayWithPersonalRating
import cinescout.rating.domain.pager.RatingsPager
import cinescout.screenplay.domain.model.ScreenplayTypeFilter
import cinescout.screenplay.domain.model.TmdbGenreId
import org.koin.core.annotation.Factory

@Factory
internal class RealRatingsPager(
    private val localDataSource: LocalPersonalRatingDataSource,
    private val remoteMediatorFactory: RatingsRemoteMediatorFactory
) : RatingsPager {

    override fun create(
        genreFilter: Option<TmdbGenreId>,
        sorting: ListSorting,
        type: ScreenplayTypeFilter
    ): Pager<Int, ScreenplayWithPersonalRating> = Pager(
        config = PagingConfig(pageSize = PagingDefaults.PageSize),
        remoteMediator = remoteMediatorFactory.create(type),
        pagingSourceFactory = { localDataSource.findPagedRatings(genreFilter, sorting, type) }
    )
}
