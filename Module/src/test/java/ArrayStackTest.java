import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayStackTest {
    @Test
    void arrayStackIsEmptyTest() {
        ArrayStack stack = ArrayStack.empty_stack();

        assertTrue(stack.is_empty());
        assertEquals(0, stack.size());
    }

    @Test
    void arrayStackPushPeekAndPopTest() {
        ArrayStack stack = ArrayStack.empty_stack();
        stack.push("first");
        stack.push("second");

        assertEquals(2, stack.size());
        assertEquals("second", stack.peek());
        assertEquals(2, stack.size());

        assertEquals("second", stack.pop());
        assertEquals("first", stack.pop());
        assertTrue(stack.is_empty());
    }

    @Test
    void arrayStackThrowsWhenEmpty() {
        ArrayStack stack = ArrayStack.empty_stack();

        assertThrows(IndexOutOfBoundsException.class, () -> stack.pop());
        assertThrows(IndexOutOfBoundsException.class, () -> stack.peek());
    }
}