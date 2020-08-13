package stats

import entities.stats.StatRepository
import org.koin.dsl.module

val statsModule = module {
    factory<StatRepository> {
        StatRepositoryImpl(
            localSource = get()
        )
    }
}
