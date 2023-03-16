package cinescout.fetchdata.data.mapper

import cinescout.database.model.DatabaseFetchKey
import cinescout.fetchdata.domain.model.FetchKey
import org.koin.core.annotation.Factory

@Factory
internal class DatabaseFetchKeyMapper {

    fun toDatabaseFetchKey(fetchKey: FetchKey): DatabaseFetchKey {
        return when (fetchKey) {
            is FetchKey.WithPage<*> ->
                DatabaseFetchKey.WithPage(fetchKey.value.toString(), fetchKey.page)
            is FetchKey.WithoutPage<*> ->
                DatabaseFetchKey.WithoutPage(fetchKey.value.toString())
        }
    }
}
