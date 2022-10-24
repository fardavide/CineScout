package cinescout.suggestions.presentation.util

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class StackTest {

    @Test
    fun `equals on empty stacks`() {
        val stack1 = Stack.empty<Int>()
        val stack2 = Stack.empty<Int>()

        assertEquals(stack1, stack2)
    }

    @Test
    fun `equals on non empty stacks`() {
        val stack1 = Stack.fromCollection(listOf(1, 2, 3))
        val stack2 = Stack.fromCollection(listOf(1, 2, 3))

        assertEquals(stack1, stack2)
    }

    @Test
    fun `equals on same content but different collections`() {
        val stack1 = Stack.fromCollection(listOf(1, 2, 3))
        val stack2 = Stack.fromCollection(setOf(1, 2, 3))

        assertEquals(stack1, stack2)
    }

    @Test
    fun `not equals on non empty stacks`() {
        val stack1 = Stack.fromCollection(listOf(1, 2, 3))
        val stack2 = Stack.fromCollection(listOf(1, 2, 4))

        assertNotEquals(stack1, stack2)
    }

    @Test
    fun `not equals on empty and non empty stacks`() {
        val stack1 = Stack.empty<Int>()
        val stack2 = Stack.fromCollection(listOf(1, 2, 3))

        assertNotEquals(stack1, stack2)
    }

    @Test
    fun `not equals on non empty stacks with same elements but different order`() {
        val stack1 = Stack.fromCollection(listOf(1, 2, 3))
        val stack2 = Stack.fromCollection(listOf(3, 2, 1))

        assertNotEquals(stack1, stack2)
    }

    @Test
    fun `toString on empty stack`() {
        val stack = Stack.empty<Int>()

        assertEquals("[]", stack.toString())
    }

    @Test
    fun `toString on non empty stack`() {
        val stack = Stack.fromCollection(listOf(1, 2, 3))

        assertEquals("[1, 2, 3]", stack.toString())
    }

    @Test
    fun `empty stack contains no elements`() {
        val stack = Stack.empty<Int>()
        assertTrue(stack.isEmpty())
    }

    @Test
    fun `stack from empty list contains no elements`() {
        val list = emptyList<Int>()
        val stack = Stack.fromCollection(list)
        assertTrue(stack.isEmpty())
    }

    @Test
    fun `stack contains declared count of elements from a list`() {
        val list = listOf(5, 6, 7, 8, 9)
        val expected = setOf(5, 6, 7, 8, 9)
        val stack = Stack.fromCollection(list)
        assertEquals(expected, stack.all())
    }

    @Test
    fun `stack contains all elements from a list for two elements when size is greater`() {
        val list = listOf(5, 6)
        val expectedElements = setOf(5, 6)
        val stack = Stack.fromCollection(list)
        assertEquals(expectedElements, stack.all())
    }

    @Test
    fun `stack doesn't allow duplications`() {
        val list = listOf(1, 2, 3, 3, 4)
        val expected = setOf(1, 2, 3, 4)
        val stack = Stack.fromCollection(list)
        assertEquals(expected, stack.all())
    }

    @Test
    fun `pop first element from stack`() {
        val stack = Stack.fromCollection(listOf(1, 2, 3))
        val expectedElements = setOf(2, 3)
        val (newStack, popped) = stack.pop()
        assertEquals(1, popped)
        assertEquals(expectedElements, newStack.all())
    }

    @Test
    fun `join a stack with a collection`() {
        val stack = Stack.fromCollection(listOf(1, 2, 3))
        val expectedElements = setOf(1, 4, 5, 6, 7, 8, 2, 3)
        val collection = listOf(4, 5, 6, 7, 8)
        val joined = stack.join(collection)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join an empty stack with a collection`() {
        val stack = Stack.empty<Int>()
        val expectedElements = setOf(4, 5, 6, 7, 8)
        val collection = listOf(4, 5, 6, 7, 8)
        val joined = stack.join(collection)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join a stack with an element`() {
        val stack = Stack.fromCollection(listOf(1, 2, 3))
        val expectedElements = setOf(1, 4, 2, 3)
        val joined = stack.join(4)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join an empty stack with an element`() {
        val stack = Stack.empty<Int>()
        val expectedElements = setOf(4)
        val joined = stack.join(4)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join a stack with an empty collection`() {
        val stack = Stack.fromCollection(listOf(1, 2, 3))
        val expectedElements = setOf(1, 2, 3)
        val collection = emptyList<Int>()
        val joined = stack.join(collection)
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join by element keeps the new element`() {
        val collection = listOf(
            TestElement(id = 1, name = "one"),
            TestElement(id = 2, name = "two"),
            TestElement(id = 3, name = "three")
        )
        val expectedElements = setOf(
            TestElement(id = 1, name = "one"),
            TestElement(id = 2, name = "updated"),
            TestElement(id = 3, name = "three")
        )
        val state = Stack.fromCollection(collection)
        val joined = state.joinBy(TestElement(id = 2, name = "updated")) { it.id }
        assertEquals(expectedElements, joined.all())
    }

    @Test
    fun `join an empty stack with an empty collection`() {
        val stack = Stack.empty<Int>()
        val collection = emptyList<Int>()
        val joined = stack.join(collection)
        assertTrue(joined.isEmpty())
        assertEquals(emptySet(), joined.all())
    }

    data class TestElement(val id: Int, val name: String)
}
