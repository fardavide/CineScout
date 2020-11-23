package domain.auth

import domain.stats.Sync
import domain.stats.SyncTraktStats
import entities.Either
import entities.NetworkError
import entities.auth.Auth.LoginError
import entities.auth.Auth.LoginState
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
import util.exhaustive
import util.test.CoroutinesTest
import kotlin.test.*

class LinkToTraktTest : CoroutinesTest {

    private val auth = mockk<TmdbAuth> {
        coEvery { login() } returns
            flowOf(LoginState.Loading, LoginState.Completed).map { it.right() }
    }
    private val sync = mockk<SyncTraktStats>(relaxed = true)
    private val link = LinkToTrakt(auth, sync)

    @Test
    fun `api test`() = coroutinesTest {

        when (val result = link().first()) {
            is Either.Left -> when (val error = result.leftOrThrow()) {
                is Link.Error.Login -> when (val loginError = error.loginError) {
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
                is Link.Error.Sync -> when (val syncError = error.syncError) {

                    else -> TODO("This is an object, implement when changed")
                }
            }
            is Either.Right -> when (val state = result.rightOrThrow()) {
                is Link.State.Login -> when (val loginState = state.loginState) {
                    LoginState.Loading -> {}
                    is LoginState.ApproveRequestToken<*> -> TODO()
                    LoginState.Completed -> TODO()
                }
                is Link.State.Sync -> when (val syncState = state.syncState) {
                    Sync.State.Loading -> TODO()
                    Sync.State.Completed -> TODO()
                }
                Link.State.None -> TODO()
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
