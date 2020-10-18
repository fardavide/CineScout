package domain.auth

import assert4k.*
import domain.stats.LaunchSyncTmdbStats
import entities.Either
import entities.NetworkError
import entities.auth.TmdbAuth
import entities.auth.TmdbAuth.LoginError.InputError.InvalidEmail
import entities.auth.TmdbAuth.LoginError.InputError.InvalidPassword
import entities.field.InvalidEmailError
import entities.field.InvalidPasswordError
import entities.left
import entities.right
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import util.test.CoroutinesTest
import kotlin.test.*
import domain.Test as TestData

class LinkToTmdbTest : CoroutinesTest {

    private val auth = mockk<TmdbAuth> {
        coEvery { login(any(), any()) } returns Unit.right()
    }
    private val sync = mockk<LaunchSyncTmdbStats>(relaxed = true)
    private val link = LinkToTmdb(auth, sync)

    @Test
    fun `api test`() = coroutinesTest {

        when (val result = link(TestData.EmailAddress.Valid, TestData.Password.Valid)) {
            is Either.Right -> {
                // success
            }
            is Either.Left -> when (val error = result.leftOrThrow()) {
                is TmdbAuth.LoginError.NetworkError -> when (error.reason) {
                    NetworkError.NoNetwork -> TODO()
                    NetworkError.Unreachable -> TODO()
                }
                is TmdbAuth.LoginError.InputError -> when (error) {
                    is InvalidEmail -> when (error.reason) {
                        InvalidEmailError.Empty -> TODO()
                        InvalidEmailError.WrongFormat -> TODO()
                    }
                    is InvalidPassword -> when (error.reason) {
                        InvalidPasswordError.EmptyPasswordError -> TODO()
                        InvalidPasswordError.ShortPasswordError -> TODO()
                    }
                }
                TmdbAuth.LoginError.WrongCredentials -> TODO()
            }
        }
    }

    @Test
    fun `InvalidEmail for Empty is returned if EmailAddress is not valid`() = coroutinesTest {

        val result = link(TestData.EmailAddress.Empty, TestData.Password.Valid)
        assert that result equals InvalidEmail(InvalidEmailError.Empty).left()
    }

    @Test
    fun `InvalidEmail for WrongFormat is returned if EmailAddress is not valid`() = coroutinesTest {

        val result = link(TestData.EmailAddress.WrongFormat, TestData.Password.Valid)
        assert that result equals InvalidEmail(InvalidEmailError.WrongFormat).left()
    }

    @Test
    fun `InvalidPassword for Empty is returned if password is blank`() = coroutinesTest {

        val result = link(TestData.EmailAddress.Valid, TestData.Password.Empty)
        assert that result equals InvalidPassword(InvalidPasswordError.EmptyPasswordError).left()
    }

    @Test
    fun `InvalidPassword for Short is returned if password is blank`() = coroutinesTest {

        val result = link(TestData.EmailAddress.Valid, TestData.Password.Short)
        assert that result equals InvalidPassword(InvalidPasswordError.ShortPasswordError).left()
    }

    @Test
    fun `WrongCredentials is returned if wrong credentials`() = coroutinesTest {
        val expected = TmdbAuth.LoginError.WrongCredentials.left()
        coEvery { auth.login(any(), any()) } returns expected

        val result = link(TestData.EmailAddress.Valid, TestData.Password.Valid)
        assert that result equals expected
    }

    @Test
    fun `launch sync if logic succeed`() = coroutinesTest {

        link(TestData.EmailAddress.Valid, TestData.Password.Valid)
        coVerify { sync() }
    }
}
