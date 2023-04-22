package cinescout.utils.compose.paging

import androidx.paging.compose.LazyPagingItems
import cinescout.test.android.PagingTestExtension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kotlin.reflect.full.callSuspend

class LazyPagingItemsUtilsTest : BehaviorSpec({
    extensions(PagingTestExtension())

    Given("a LazyPagingItems created unsafely from a collections of items") {
        val lazyPagingItems = unsafeLazyPagingItemsOf("a", "b", "c")

        When("collecting the items") {
            lazyPagingItems.unsafeCollectPagingData()

            Then("itemCount should be 3") {
                lazyPagingItems.itemCount shouldBe 3
            }

            Then("the items should be in the LazyPagingItems") {
                lazyPagingItems.itemSnapshotList shouldBe listOf("a", "b", "c")
            }
        }
    }
})

/**
 * Calls internal method [LazyPagingItems.collectPagingData] using reflection.
 */
private suspend fun <T : Any> LazyPagingItems<T>.unsafeCollectPagingData() {
    val collectPagingData = this::class.members.first { it.name == "collectPagingData" }
    collectPagingData.callSuspend(this)
}
