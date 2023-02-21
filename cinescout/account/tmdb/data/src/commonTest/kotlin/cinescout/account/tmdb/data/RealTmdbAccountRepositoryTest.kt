package cinescout.account.tmdb.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.account.tmdb.domain.TmdbAccountRepository
import cinescout.error.NetworkError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import store.Refresh
import store.StoreOwner
import store.test.MockStoreOwner

class RealTmdbAccountRepositoryTest : BehaviorSpec({

    Given("no account cached") {
        val cachedAccount: Account.Tmdb? = null

        And("no account connected") {
            val remoteAccount: Account.Tmdb? = null


            When("getting account") {
                val repository = TestScenario(
                    cachedAccount = cachedAccount,
                    remoteAccount = remoteAccount
                )
                repository.getAccount(Refresh.IfNeeded).test {

                    Then("no account connected error") {
                        awaitItem() shouldBe GetAccountError.NotConnected.left()
                    }
                }
            }
        }

        And("account connected") {
            val remoteAccount = AccountSample.Tmdb

            When("getting account") {
                val repository = TestScenario(
                    cachedAccount = cachedAccount,
                    remoteAccount = remoteAccount
                )
                repository.getAccount(Refresh.IfNeeded).test {

                    Then("account connected") {
                        awaitItem() shouldBe remoteAccount.right()
                    }
                }
            }

            When("sync") {
                val scenario = TestScenario(
                    cachedAccount = cachedAccount,
                    remoteAccount = remoteAccount
                )
                scenario.syncAccount()

                Then("account connected") {
                    scenario.localDataSource.findAccount().first() shouldBe remoteAccount
                }
            }
        }

        And("error getting the remote account") {
            val remoteAccountError = NetworkError.NoNetwork

            When("getting account") {
                val repository = TestScenario(
                    cachedAccount = cachedAccount,
                    remoteAccountError = remoteAccountError
                )
                repository.getAccount(Refresh.IfNeeded).test {

                    Then("network error") {
                        awaitItem() shouldBe GetAccountError.Network(remoteAccountError).left()
                    }
                }
            }
        }

        When("removing account") {
            val scenario = TestScenario(
                cachedAccount = cachedAccount,
                remoteAccount = null
            )
            scenario.removeAccount()

            Then("no account connected") {
                scenario.localDataSource.findAccount().first() shouldBe null
            }
        }
    }

    Given("cached account") {
        val cachedAccount = AccountSample.Tmdb
        val storeOwner = MockStoreOwner().updated()

        And("account connected") {
            val remoteAccount = AccountSample.Tmdb

            When("getting account") {
                val repository = TestScenario(
                    cachedAccount = cachedAccount,
                    remoteAccount = remoteAccount,
                    storeOwner = storeOwner
                )
                repository.getAccount(Refresh.IfNeeded).test {

                    Then("account connected") {
                        awaitItem() shouldBe cachedAccount.right()
                    }
                }
            }
        }

        And("error getting the remote account") {
            val remoteAccountError = NetworkError.NoNetwork

            When("getting account") {
                val repository = TestScenario(
                    cachedAccount = cachedAccount,
                    remoteAccountError = remoteAccountError,
                    storeOwner = storeOwner
                )
                repository.getAccount(Refresh.IfNeeded).test {

                    Then("returns cached account") {
                        awaitItem() shouldBe cachedAccount.right()
                    }
                }
            }
        }

        When("removing account") {
            val scenario = TestScenario(
                cachedAccount = cachedAccount,
                remoteAccount = null,
                storeOwner = storeOwner
            )
            scenario.removeAccount()

            Then("no account connected") {
                scenario.localDataSource.findAccount().first() shouldBe null
            }
        }
    }
})

private class TestScenario(
    sut: RealTmdbAccountRepository,
    val localDataSource: TmdbAccountLocalDataSource
) : TmdbAccountRepository by sut

private fun TestScenario(
    cachedAccount: Account.Tmdb?,
    remoteAccount: Account.Tmdb?,
    storeOwner: StoreOwner = MockStoreOwner()
): TestScenario {
    val localDataSource = FakeTmdbAccountLocalDataSource(account = cachedAccount)
    return TestScenario(
        sut = RealTmdbAccountRepository(
            localDataSource = localDataSource,
            remoteDataSource = FakeTmdbAccountRemoteDataSource(account = remoteAccount),
            storeOwner = storeOwner
        ),
        localDataSource = localDataSource
    )
}

private fun TestScenario(
    cachedAccount: Account.Tmdb?,
    remoteAccountError: NetworkError,
    storeOwner: StoreOwner = MockStoreOwner()
): TestScenario {
    val localDataSource = FakeTmdbAccountLocalDataSource(account = cachedAccount)
    return TestScenario(
        sut = RealTmdbAccountRepository(
            localDataSource = localDataSource,
            remoteDataSource = FakeTmdbAccountRemoteDataSource(networkError = remoteAccountError),
            storeOwner = storeOwner
        ),
        localDataSource = localDataSource
    )
}
