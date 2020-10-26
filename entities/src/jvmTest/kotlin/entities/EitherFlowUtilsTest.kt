package entities

import assert4k.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import util.test.CoroutinesTest
import kotlin.test.*

internal class EitherFlowUtilsTest : CoroutinesTest {

    @Test
    fun `foldMap works correctly`() = coroutinesTest {
        val source = flowOf(1.left(), "2".right())
        val result = source.foldMap({ it.toString() }, { it.toInt() })

        val (first, second) = result.toList()
        assert that first *{
            +leftOrNull() `is` type<String>()
            +leftOrNull() equals 1
            +rightOrNull() `is` Null
        }
        assert that second *{
            +rightOrNull() `is` type<Int>()
            +rightOrNull() equals 2
            +leftOrNull() `is` Null
        }
    }

    // region fix
    @Test
    fun `fix short circuits with single static flow`() = coroutinesTest {
        val source = flowOf(1.right(), 2.right(), "hello".left(), 4.right())
        val result = source.fix()

        val items = result.toList()
        assert that items equals listOf(1.right(), 2.right(), "hello".left())
    }

    @Test
    fun `fix short circuits on first flow of multiple dynamic flows`() = coroutinesTest {
        val first = flow {
            emit(1.right())
            emit(2.right())
            emit("hello".left())
            emit(4.right())
        }
        val second = flow {
            emit(5.right())
            emit(6.right())
        }
        val result = (first + second).fix()

        val items = result.toList()
        assert that items equals listOf(1.right(), 2.right(), "hello".left())
    }

    @Test
    fun `fix short circuits on second flow of multiple dynamic flows`() = coroutinesTest {
        val first = flow {
            emit(1.right())
            emit(2.right())
            emit(3.right())
        }
        val second = flow {
            emit(4.right())
            emit("hello".left())
            emit(6.right())
        }
        val result = (first + second).fix()

        val items = result.toList()
        assert that items equals listOf(1.right(), 2.right(), 3.right(), 4.right(), "hello".left())
    }
    // endregion

    // region plus
    @Test
    fun `plus concatenate with static flows`() = coroutinesTest {
        val source = flowOf(1, 2, 3).map { it.right() }
        val result = source + flowOf(4, 5).map { it.right() }

        val items = result.toList()
        assert that items equals listOf(1, 2, 3, 4, 5).map { it.right() }
    }

    @Test
    fun `plus concatenate with dynamic flows`() = coroutinesTest {
        val first = flow {
            emit(1.right())
            emit(2.right())
            emit(3.right())
        }
        val second = flow {
            emit(4.right())
            emit(5.right())
        }
        val result = first + second

        val items = result.toList()
        assert that items equals listOf(1, 2, 3, 4, 5).map { it.right() }
    }
    @Test
    fun `plus short circuits on first flow`() = coroutinesTest {
        val first = flowOf(
            1.right(),
            2.right(),
            "hello".left(),
            4.right()
        )
        val second = flowOf(
            5.right(),
            6.right()
        )
        val result = first + second

        val items = result.toList()
        assert that items equals listOf(1.right(), 2.right(), "hello".left())
    }

    @Test
    fun `plus short circuits on second flow`() = coroutinesTest {
        val first = flowOf(
            1.right(),
            2.right(),
            3.right()
        )
        val second = flowOf(
            4.right(),
            "hello".left(),
            6.right()
        )
        val result = first + second

        val items = result.toList()
        assert that items equals listOf(1.right(), 2.right(), 3.right(), 4.right(), "hello".left())
    }

    @Test
    fun `lazy plus defers the work for the second flow`() = coroutinesTest {
        val first = flowOf(1.right(), "hello".left())
        var called = false
        val second = flow {
            called = true
            emit(4.right())
            emit(5.right())
        }
        val result = (first + { second }).fix()

        result.collect()
        assert that called.not()
    }
    // endregion

    // region then
    @Test
    fun `then concatenate with last element of static flows`() = coroutinesTest {
        val source = flowOf(1, 2, 3).map { it.right() }
        val result = source.then(3, flowOf(4, 5).map { it.right() })

        val items = result.toList()
        assert that items equals listOf(1, 2, 3, 4, 5).map { it.right() }
    }

    @Test
    fun `then concatenate with last element of dynamic flows`() = coroutinesTest {
        val first = flow {
            emit(1.right())
            emit(2.right())
            emit(3.right())
        }
        val second = flow {
            emit(4.right())
            emit(5.right())
        }
        val result = first.then(3, second)

        val items = result.toList()
        assert that items equals listOf(1, 2, 3, 4, 5).map { it.right() }
    }

    @Test
    fun `then concatenate with intermediate element of static flows`() = coroutinesTest {
        val source = flowOf(1, 2, 3).map { it.right() }
        val result = source.then(2, { flowOf(4, 5).map { it.right() } })

        val items = result.toList()
        assert that items equals listOf(1, 2, 4, 5).map { it.right() }
    }

    @Test
    fun `then concatenate with intermediate element of dynamic flows`() = coroutinesTest {
        val first = flow {
            emit(1.right())
            emit(2.right())
            emit(3.right())
        }
        val second = flow {
            emit(4.right())
            emit(5.right())
        }
        val result = first.then(2, second)

        val items = result.toList()
        assert that items equals listOf(1, 2, 4, 5).map { it.right() }
    }
    @Test
    fun `then short circuits on first flow`() = coroutinesTest {
        val first = flowOf(
            1.right(),
            2.right(),
            "hello".left(),
            4.right()
        )
        val second = flowOf(
            5.right(),
            6.right()
        )
        val result = first.then(3, second)

        val items = result.toList()
        assert that items equals listOf(1.right(), 2.right(), "hello".left())
    }

    @Test
    fun `then short circuits on second flow`() = coroutinesTest {
        val first = flowOf(
            1.right(),
            2.right(),
            3.right()
        )
        val second = flowOf(
            4.right(),
            "hello".left(),
            6.right()
        )
        val result = first.then(2, second)

        val items = result.toList()
        assert that items equals listOf(1.right(), 2.right(), 4.right(), "hello".left())
    }

    @Test
    fun `lazy then defers the work for the second flow`() = coroutinesTest {
        val first = flowOf(1.right(), "hello".left(), 2.right())
        var called = false
        val second = flow {
            called = true
            emit(4.right())
            emit(5.right())
        }
        val result = (first.then(2) { second }).fix()

        result.collect()
        assert that called.not()
    }
    // endregion

}
