package auth.credentials

import database.Database
import entities.auth.CredentialRepository
import org.koin.dsl.module

val authCredentialsModule = module {

    single<CredentialRepository> { CredentialRepositoryImpl(tmdbCredentials = get()) }

    factory { get<Database>().tmdbCredentialQueries }
}
