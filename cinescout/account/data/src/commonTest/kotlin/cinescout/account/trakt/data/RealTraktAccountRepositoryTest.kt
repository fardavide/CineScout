package cinescout.account.trakt.data

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.account.domain.AccountRepository
import cinescout.account.domain.model.Account
import cinescout.account.domain.model.GetAccountError
import cinescout.account.domain.sample.AccountSample
import cinescout.error.NetworkError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import store.Refresh
import store.StoreOwner
import store.test.MockStoreOwner

class RealTraktAccountRepositoryTest : BehaviorSpec({

    Given("no account cached") {
        val cachedAccount: Account? = null

        And("no account connected") {
            val remoteAccount: Account? = null


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
            val remoteAccount = AccountSample.Trakt

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
        val cachedAccount = AccountSample.Trakt
        val storeOwner = MockStoreOwner().updated()

        And("account connected") {
            val remoteAccount = AccountSample.Trakt

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
    sut: RealAccountRepository,
    val localDataSource: TraktAccountLocalDataSource
) : AccountRepository by sut

private fun TestScenario(
    cachedAccount: Account?,
    remoteAccount: Account?,
    storeOwner: StoreOwner = MockStoreOwner()
): TestScenario {
    val localDataSource = FakeTraktAccountLocalDataSource(account = cachedAccount)
    return TestScenario(
        sut = RealAccountRepository(
            localDataSource = localDataSource,
            remoteDataSource = FakeTraktAccountRemoteDataSource(account = remoteAccount),
            storeOwner = storeOwner
        ),
        localDataSource = localDataSource
    )
}

private fun TestScenario(
    cachedAccount: Account?,
    remoteAccountError: NetworkError,
    storeOwner: StoreOwner = MockStoreOwner()
): TestScenario {
    val localDataSource = FakeTraktAccountLocalDataSource(account = cachedAccount)
    return TestScenario(
        sut = RealAccountRepository(
            localDataSource = localDataSource,
            remoteDataSource = FakeTraktAccountRemoteDataSource(networkError = remoteAccountError),
            storeOwner = storeOwner
        ),
        localDataSource = localDataSource
    )
}
