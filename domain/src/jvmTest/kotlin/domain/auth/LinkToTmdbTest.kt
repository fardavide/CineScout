package domain.auth

import domain.stats.SyncTmdbStats
import entities.Either
import entities.NetworkError
import entities.auth.TmdbAuth
import entities.auth.TmdbAuth.LoginError
import entities.auth.TmdbAuth.LoginState
import entities.left
import entities.right
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import util.exhaustive
import util.test.CoroutinesTest
import kotlin.test.*

class LinkToTmdbTest : CoroutinesTest {

    private val auth = mockk<TmdbAuth> {
        coEvery { login() } returns
            flowOf(LoginState.Loading, LoginState.Completed).map { it.right() }
    }
    private val sync = mockk<SyncTmdbStats>(relaxed = true)
    private val link = LinkToTmdb(auth, sync)

    @Test
    fun `api test`() = coroutinesTest {

        when (val result = link().first()) {
            is Either.Left -> when (val error = result.leftOrThrow()) {
                is LinkToTmdb.Error.Login -> when (val loginError = error.loginError) {
                    LoginError.TokenApprovalCancelled -> TODO()
                    is LoginError.NetworkError -> when (val networkError = loginError.reason) {
                        NetworkError.Forbidden -> TODO()
                        NetworkError.NoNetwork -> TODO()
                        NetworkError.NotFound -> TODO()
                        NetworkError.Internal -> TODO()
                        NetworkError.Unauthorized -> TODO()
                        NetworkError.Unreachable -> TODO()
                    }
                }
                is LinkToTmdb.Error.Sync -> when (val syncError = error.syncError) {

                    else -> TODO("This is an object, implement when changed")
                }
            }
            is Either.Right -> when (val state = result.rightOrThrow()) {
                is LinkToTmdb.State.Login -> when (val loginState = state.loginState) {
                    LoginState.Loading -> {}
                    is LoginState.ApproveRequestToken -> TODO()
                    LoginState.Completed -> TODO()
                }
                is LinkToTmdb.State.Sync -> when (val syncState = state.syncState) {
                    SyncTmdbStats.State.Loading -> TODO()
                    SyncTmdbStats.State.Completed -> TODO()
                }
                LinkToTmdb.State.None -> TODO()
            }
        }.exhaustive
    }

    @Test
    fun `launch sync if logic succeed`() = coroutinesTest {
        link().collect()
        coVerify(exactly = 1) { sync() }
    }

    @Test
    fun `does not launch sync if logic didn't succeed`() = coroutinesTest {
        coEvery { auth.login() } returns
            flowOf(LoginState.Loading.right(), LoginError.NetworkError(NetworkError.NoNetwork).left())

        link().collect()
        coVerify(exactly = 0) { sync() }
    }
}
