package entities.auth

import kotlinx.coroutines.flow.Flow

interface TraktAuth {

    fun login(): Flow<Either_LoginResult>
}
