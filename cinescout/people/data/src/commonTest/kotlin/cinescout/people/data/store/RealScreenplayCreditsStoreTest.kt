package cinescout.people.data.store

import app.cash.turbine.test
import arrow.core.right
import cinescout.fetchdata.domain.model.FetchData
import cinescout.fetchdata.domain.repository.FakeFetchDataRepository
import cinescout.people.data.datasource.FakeLocalPeopleDataSource
import cinescout.people.data.datasource.FakeRemotePeopleDataSource
import cinescout.people.domain.model.ScreenplayCredits
import cinescout.people.domain.sample.ScreenplayCreditsSample
import cinescout.people.domain.store.ScreenplayCreditsStore
import cinescout.screenplay.domain.model.ids.TmdbScreenplayId
import cinescout.screenplay.domain.sample.ScreenplayIdsSample
import cinescout.store5.Store5ReadResponse
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import korlibs.time.DateTime
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin

class RealScreenplayCreditsStoreTest : BehaviorSpec({
    coroutineTestScope = true

    val id: TmdbScreenplayId = ScreenplayIdsSample.Dexter.tmdb
    val credits = ScreenplayCreditsSample.Dexter

    Given("local data") {

        When("when refresh is true") {
            val scenario = TestScenario(
                localCredits = credits,
                remoteCredits = credits
            )
            val request = StoreReadRequest.cached(ScreenplayCreditsStore.Key(id), refresh = true)
            scenario.sut.stream(request).test {

                Then("remote data is returned") {
                    with(awaitItem()) {
                        origin shouldBe StoreReadResponseOrigin.SourceOfTruth
                        dataOrNull() shouldBe credits.right()
                    }
                    with(awaitItem()) {
                        origin shouldBe StoreReadResponseOrigin.Fetcher()
                        shouldBeInstanceOf<Store5ReadResponse.Loading>()
                    }
                    with(awaitItem()) {
                        origin shouldBe StoreReadResponseOrigin.Fetcher()
                        dataOrNull() shouldBe credits.right()
                    }
                }
            }
        }

        When("when refresh is false") {
            val scenario = TestScenario(
                localCredits = credits,
                remoteCredits = credits
            )
            val request = StoreReadRequest.cached(ScreenplayCreditsStore.Key(id), refresh = false)
            scenario.sut.stream(request).test {

                Then("local data is returned") {
                    with(awaitItem()) {
                        origin shouldBe StoreReadResponseOrigin.SourceOfTruth
                        dataOrNull() shouldBe credits.right()
                    }
                }
            }
        }
    }

    Given("no local data") {

        When("when refresh is true") {
            val scenario = TestScenario(
                localCredits = null,
                remoteCredits = credits
            )
            val request = StoreReadRequest.cached(ScreenplayCreditsStore.Key(id), refresh = true)
            scenario.sut.stream(request).test {

                Then("remote data is returned") {
                    with(awaitItem()) {
                        origin shouldBe StoreReadResponseOrigin.Fetcher()
                        shouldBeInstanceOf<Store5ReadResponse.Loading>()
                    }
                    with(awaitItem()) {
                        origin shouldBe StoreReadResponseOrigin.Fetcher()
                        dataOrNull() shouldBe credits.right()
                    }
                }
            }
        }

        When("when refresh is false") {
            val scenario = TestScenario(
                localCredits = null,
                remoteCredits = credits
            )
            val request = StoreReadRequest.cached(ScreenplayCreditsStore.Key(id), refresh = false)
            scenario.sut.stream(request).test {

                Then("remote data is returned") {
                    with(awaitItem()) {
                        origin shouldBe StoreReadResponseOrigin.Fetcher()
                        shouldBeInstanceOf<Store5ReadResponse.Loading>()
                    }
                    with(awaitItem()) {
                        origin shouldBe StoreReadResponseOrigin.Fetcher()
                        dataOrNull() shouldBe credits.right()
                    }
                }
            }
        }
    }
})

private class RealScreenplayCreditsStoreTestScenario(
    val sut: RealScreenplayCreditsStore
)

private fun TestScenario(
    localCredits: ScreenplayCredits? = null,
    remoteCredits: ScreenplayCredits
): RealScreenplayCreditsStoreTestScenario {
    val fetchData = localCredits
        ?.let {
            mapOf(ScreenplayCreditsStore.Key(it.screenplayId) to FetchData(dateTime = DateTime.EPOCH))
        }
        ?: emptyMap()
    return RealScreenplayCreditsStoreTestScenario(
        sut = RealScreenplayCreditsStore(
            fetchDataRepository = FakeFetchDataRepository(data = fetchData),
            localPeopleDataSource = FakeLocalPeopleDataSource(credits = localCredits),
            remotePeopleDataSource = FakeRemotePeopleDataSource(credits = remoteCredits)
        )
    )
}
