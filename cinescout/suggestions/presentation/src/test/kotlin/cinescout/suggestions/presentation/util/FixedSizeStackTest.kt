package cinescout.suggestions.presentation.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FixedSizeStackTest {

    @Test
    fun `empty stack contains no elements`() {
        val stack = FixedSizeStack.empty<Int>(size = 3)
        assertTrue(stack.isEmpty())
    }

    @Test
    fun `stack from empty list contains no elements`() {
        val list = emptyList<Int>()
        val stack = FixedSizeStack.fromCollection(size = 3, list)
        assertTrue(stack.isEmpty())
    }

    @Test
    fun `stack contains declared count of elements from a list`() {
        val list = listOf(5, 6, 7, 8, 9)
        val expected = setOf(5, 6, 7)
        val stack = FixedSizeStack.fromCollection(size = 3, list)
        assertEquals(expected, stack.all())
    }

    @Test
    fun `stack contains all elements from a list for two elements when size is greater`() {
        val list = listOf(5, 6)
        val expectedElements = setOf(5, 6)
        val stack = FixedSizeStack.fromCollection(size = 3, list)
        assertEquals(expectedElements, stack.all())
    }

    @Test
    fun `stack doesn't allow duplications`() {
        val list = listOf(1, 2, 3, 3, 4)
        val expected = setOf(1, 2, 3, 4)
        val stack = FixedSizeStack.fromCollection(size = 5, list)
        assertEquals(expected, stack.all())
    }

    @Test
    fun `is full returns true when no element is null`() {
        val stack = FixedSizeStack.fromCollection(size = 3, listOf(1, 2, 3))
        assertTrue(stack.isFull())
    }

    @Test
    fun `is full returns false when one or more element is null`() {
        val stack = FixedSizeStack.fromCollection(size = 3, listOf(1, 2))
        assertFalse(stack.isFull())
    }

    @Test
    fun `pop first element from stack`() {
        val stack = FixedSizeStack.fromCollection(size = 3, listOf(1, 2, 3))
        val expectedElements = setOf(2, 3)
        val (newStack, popped) = stack.pop()
        assertEquals(1, popped)
        assertEquals(expectedElements, newStack.all())
    }

    @Test
    fun `join a stack with a collection`() {
        val stack = FixedSizeStack.fromCollection(size = 3, listOf(1, 2, 3))
        val expectedElements = setOf(1, 4, 5)
        val collection = listOf(4, 5, 6, 7, 8)
        val joined = stack.join(collection)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join an empty stack with a collection`() {
        val stack = FixedSizeStack.empty<Int>(size = 3)
        val expectedElements = setOf(4, 5, 6)
        val collection = listOf(4, 5, 6, 7, 8)
        val joined = stack.join(collection)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join a stack with an element`() {
        val stack = FixedSizeStack.fromCollection(size = 3, listOf(1, 2, 3))
        val expectedElements = setOf(1, 4, 2)
        val joined = stack.join(4)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join an empty stack with an element`() {
        val stack = FixedSizeStack.empty<Int>(size = 3)
        val expectedElements = setOf(4)
        val joined = stack.join(4)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join a stack with an empty collection`() {
        val stack = FixedSizeStack.fromCollection(size = 3, listOf(1, 2, 3))
        val expectedElements = setOf(1, 2, 3)
        val collection = emptyList<Int>()
        val joined = stack.join(collection)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join an empty stack with an empty collection`() {
        val stack = FixedSizeStack.empty<Int>(size = 3)
        val collection = emptyList<Int>()
        val joined = stack.join(collection)
        assertTrue(joined.isEmpty())
        assertEquals(emptySet(), joined.all())
    }
}
