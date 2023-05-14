package cinescout.store5

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import cinescout.error.NetworkError
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

class DslTest : BehaviorSpec({

    Given("a successful fetcher") {
        val apiCall = { key: Key -> Data("hello ${key.value}").right() }
        val fetcher = EitherFetcher.of<Key, Data> { key -> apiCall(key) }

        And("a SUT") {
            val flow = MutableStateFlow<MutableMap<Key, Data>>(mutableMapOf())
            val insert: suspend (key: Key, value: Data) -> Unit = { key, value ->
                val map = flow.value.apply {
                    put(key, value)
                }
                flow.emit(map)
            }
            val sut = SourceOfTruth.of(
                reader = { key -> flow.map { it[key] } },
                writer = insert
            )

            And("a Store 5") {
                val store = Store5Builder
                    .from(fetcher, sut)
                    .build()

                When("fresh stream") {
                    val request = StoreReadRequest.fresh(Key("Davide"))

                    Then("returns from remote") {
                        store.stream(request).test {
                            awaitItem() shouldBe Store5ReadResponse.Loading(origin = StoreReadResponseOrigin.Fetcher())
                            awaitItem().dataOrNull() shouldBe Data("hello Davide").right()
                        }
                    }
                }

                When("cached stream with refresh") {
                    val request = StoreReadRequest.cached(Key("Giuseppe"), refresh = true)

                    Then("returns from remote") {
                        store.stream(request).test {
                            awaitItem() shouldBe Store5ReadResponse.Loading(origin = StoreReadResponseOrigin.Fetcher())
                            awaitItem().dataOrNull() shouldBe Data("hello Giuseppe").right()
                        }
                    }
                }

                When("cached stream without refresh") {
                    val request = StoreReadRequest.cached(Key("Farella"), refresh = false)

                    Then("returns from remote if no cache") {
                        store.stream(request).test {
                            awaitItem() shouldBe Store5ReadResponse.Loading(origin = StoreReadResponseOrigin.Fetcher())
                            awaitItem().dataOrNull() shouldBe Data("hello Farella").right()
                        }
                    }
                }
            }
        }
    }

    Given("a failing fetcher") {
        val networkError = NetworkError.Forbidden
        val apiCall = { _: Key -> networkError.left() }
        val fetcher = EitherFetcher.of<Key, Data> { key -> apiCall(key) }

        And("a SUT") {
            val flow = MutableStateFlow<MutableMap<Key, Data>>(mutableMapOf())
            val insert: suspend (key: Key, value: Data) -> Unit = { key, value ->
                val map = flow.value.apply {
                    put(key, value)
                }
                flow.emit(map)
            }
            val sut = SourceOfTruth.of(
                reader = { key -> flow.map { it[key] } },
                writer = insert
            )

            And("a Store 5") {
                val store = Store5Builder
                    .from(fetcher, sut)
                    .build()

                When("fresh") {
                    val request = StoreReadRequest.fresh(Key("Davide"))

                    Then("returns from remote") {
                        store.stream(request).test {
                            awaitItem() shouldBe Store5ReadResponse.Loading(origin = StoreReadResponseOrigin.Fetcher())
                            awaitItem().dataOrNull() shouldBe networkError.left()
                        }
                    }
                }

                When("cached with refresh") {
                    val request = StoreReadRequest.cached(Key("Giuseppe"), refresh = true)

                    Then("returns from remote") {
                        store.stream(request).test {
                            awaitItem() shouldBe Store5ReadResponse.Loading(origin = StoreReadResponseOrigin.Fetcher())
                            awaitItem().dataOrNull() shouldBe networkError.left()
                        }
                    }
                }

                When("cached without refresh") {
                    val request = StoreReadRequest.cached(Key("Farella"), refresh = false)

                    Then("returns from remote if no cache") {
                        store.stream(request).test {
                            awaitItem() shouldBe Store5ReadResponse.Loading(origin = StoreReadResponseOrigin.Fetcher())
                            awaitItem().dataOrNull() shouldBe networkError.left()
                        }
                    }
                }
            }
        }
    }
})

@JvmInline value class Key(val value: String)

@JvmInline value class Data(val value: String)
