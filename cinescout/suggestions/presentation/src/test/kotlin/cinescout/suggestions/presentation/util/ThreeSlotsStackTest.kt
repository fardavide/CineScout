package cinescout.suggestions.presentation.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ThreeSlotsStackTest {

    @Test
    fun `empty stack contains no elements`() {
        val stack = ThreeSlotsStack.empty<Int>()
        assertNull(stack.first)
        assertNull(stack.second)
        assertNull(stack.third)
    }

    @Test
    fun `stack from empty list contains no elements`() {
        val list = emptyList<Int>()
        val stack = ThreeSlotsStack.from(list)
        assertNull(stack.first)
        assertNull(stack.second)
        assertNull(stack.third)
    }

    @Test
    fun `stack contains first three elements from a list`() {
        val list = listOf(5, 6, 7, 8, 9)
        val stack = ThreeSlotsStack.from(list)
        assertEquals(5, stack.first)
        assertEquals(6, stack.second)
        assertEquals(7, stack.third)
    }

    @Test
    fun `stack contains first three elements from a list for two elements`() {
        val list = listOf(5, 6)
        val stack = ThreeSlotsStack.from(list)
        assertEquals(5, stack.first)
        assertEquals(6, stack.second)
        assertEquals(null, stack.third)
    }

    @Test
    fun `is full returns true when no element is null`() {
        val stack = ThreeSlotsStack(first = 1, second = 2, third = 3)
        assertTrue(stack.isFull())
    }

    @Test
    fun `is full returns false when one or more element is null`() {
        val stack = ThreeSlotsStack(first = 1, second = 2, third = null)
        assertFalse(stack.isFull())
    }

    @Test
    fun `pop first element from stack`() {
        val stack = ThreeSlotsStack(1, 2, 3)
        val (newStack, popped) = stack.pop()
        assertEquals(1, popped)
        assertEquals(2, newStack.first)
        assertEquals(3, newStack.second)
        assertEquals(null, newStack.third)
    }

    @Test
    fun `join a stack with a collection`() {
        val stack = ThreeSlotsStack(1, 2, 3)
        val collection = listOf(4, 5, 6, 7, 8)
        val joined = stack.join(collection)
        assertEquals(1, joined.first)
        assertEquals(4, joined.second)
        assertEquals(5, joined.third)
    }

    @Test
    fun `join an empty stack with a collection`() {
        val stack = ThreeSlotsStack.empty<Int>()
        val collection = listOf(4, 5, 6, 7, 8)
        val joined = stack.join(collection)
        assertEquals(4, joined.first)
        assertEquals(5, joined.second)
        assertEquals(6, joined.third)
    }

    @Test
    fun `join a stack with an element`() {
        val stack = ThreeSlotsStack(1, 2, 3)
        val joined = stack.join(4)
        assertEquals(1, joined.first)
        assertEquals(4, joined.second)
        assertEquals(2, joined.third)
    }

    @Test
    fun `join an empty stack with an element`() {
        val stack = ThreeSlotsStack.empty<Int>()
        val joined = stack.join(4)
        assertEquals(4, joined.first)
        assertEquals(null, joined.second)
        assertEquals(null, joined.third)
    }

    @Test
    fun `join a stack with an empty collection`() {
        val stack = ThreeSlotsStack(1, 2, 3)
        val collection = emptyList<Int>()
        val joined = stack.join(collection)
        assertEquals(1, joined.first)
        assertEquals(2, joined.second)
        assertEquals(3, joined.third)
    }

    @Test
    fun `join an empty stack with an empty collection`() {
        val stack = ThreeSlotsStack.empty<Int>()
        val collection = emptyList<Int>()
        val joined = stack.join(collection)
        assertNull(joined.first)
        assertNull(joined.second)
        assertNull(joined.third)
    }
}
