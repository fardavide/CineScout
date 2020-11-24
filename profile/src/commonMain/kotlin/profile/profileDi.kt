package profile

import entities.profile.ProfileRepository
import org.koin.dsl.module

val profileModule = module {

    factory<ProfileRepository> { ProfileRepositoryImpl(tmdbProfileRepository = get(), traktProfileRepository = get()) }
}
