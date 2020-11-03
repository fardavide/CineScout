package domain.auth

import domain.stats.LaunchSyncTmdbStats
import domain.stats.SyncTmdbStats
import entities.Either
import entities.NetworkError
import entities.auth.TmdbAuth
import entities.left
import entities.right
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import util.test.CoroutinesTest
import kotlin.test.*

class LinkToTmdbTest : CoroutinesTest {

    private val auth = mockk<TmdbAuth> {
        coEvery { login() } returns
            flowOf(TmdbAuth.LoginState.Loading, TmdbAuth.LoginState.Completed).map { it.right() }
    }
    private val sync = mockk<LaunchSyncTmdbStats>(relaxed = true)
    private val link = LinkToTmdb(auth, sync)

    @Test
    fun `api test`() = coroutinesTest {

        when (val result = link().first()) {
            is Either.Left -> when (val error = result.leftOrThrow()) {
                is LinkToTmdb.Error.Login -> when (val loginError = error.loginError) {
                    NetworkError.Forbidden -> TODO()
                    NetworkError.NoNetwork -> TODO()
                    NetworkError.NotFound -> TODO()
                    NetworkError.Internal -> TODO()
                    NetworkError.Unauthorized -> TODO()
                    NetworkError.Unreachable -> TODO()
                }
                is LinkToTmdb.Error.Sync -> when (val syncError = error.syncError) {

                    else -> TODO("This is an object, implement when changed")
                }
            }
            is Either.Right -> when (val state = result.rightOrThrow()) {
                is LinkToTmdb.State.Login -> when (val loginState = state.loginState) {
                    TmdbAuth.LoginState.Loading -> TODO()
                    TmdbAuth.LoginState.RequestToken -> TODO()
                    TmdbAuth.LoginState.Completed -> TODO()
                }
                is LinkToTmdb.State.Sync -> when (val syncState = state.syncState) {
                    SyncTmdbStats.State.Loading -> TODO()
                    SyncTmdbStats.State.Completed -> TODO()
                }
            }
        }
    }

    @Test
    fun `launch sync if logic succeed`() = coroutinesTest {
        link().collect()
        coVerify(exactly = 1) { sync() }
    }

    @Test
    fun `does not launch sync if logic didn't succeed`() = coroutinesTest {
        coEvery { auth.login() } returns
            flowOf(TmdbAuth.LoginState.Loading.right(), NetworkError.NoNetwork.left())

        link().collect()
        coVerify(exactly = 0) { sync() }
    }
}
