import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ASTTest {
    @Test
    void numberEvaluatesTest() {
        assertEquals(2.5, new NumNode(2.5).eval());
    }

    @Test
    void binaryEvaluateTest() {
        AST two = new NumNode(2);
        AST three = new NumNode(3);
        assertEquals(5, new BinopNode("+", two, three).eval());
        assertEquals(-1, new BinopNode("-", two, three).eval());
        assertEquals(6, new BinopNode("*", two, three).eval());
        assertEquals(1.5, new BinopNode("/", three, two).eval());
        assertEquals(8, new BinopNode("^", two, three).eval());
    }

    @Test
    void nestedTreeEvaluatesTest() {
        AST tree = new BinopNode("*", new NumNode(2),
                new BinopNode("+", new NumNode(3), new NumNode(4)));
        assertEquals(14, tree.eval());
    }

    @Test
    void unsupportedOperatorTest() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new BinopNode("%", new NumNode(2), new NumNode(3)).eval());
        assertEquals("invalid token", error.getMessage());
    }
}
