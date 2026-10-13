import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private static void postfixError(String input, String message) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Parser.parsePostfix(input));
        assertEquals(message, error.getMessage());
    }

    private static void infixError(String input, String message) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> Parser.parseInfix(input));
        assertEquals(message, error.getMessage());
    }

    @Test
    void postfixBuildsTreesTest() {
        assertEquals(14, Parser.parsePostfix("2 3 4 + *").eval());
        assertEquals(-1, Parser.parsePostfix("2 3 -").eval());
        assertEquals(1.5, Parser.parsePostfix("3 2 /").eval());
        assertEquals(512, Parser.parsePostfix("2 3 2 ^ ^").eval());
        assertEquals(-2.5, Parser.parsePostfix(" -2.5 ").eval());
    }

    @Test
    void postfixReportsTest() {
        postfixError("  ", "empty input");
        postfixError("2 a +", "invalid token");
        postfixError("2 +", "insufficient operands");
        postfixError("2 3 4 +", "too many operands");
    }

    @Test
    void infixHandlesPrecedenceTest() {
        assertEquals(14, Parser.parseInfix("2* ( 3 + 4 )").eval());
        assertEquals(128, Parser.parseInfix("2 * 4 ^ 3").eval());
        assertEquals(512, Parser.parseInfix("2 ^ 3 ^ 2").eval());
        assertEquals(1.5, Parser.parseInfix("2 * 3 / 4").eval());
        assertEquals(1, Parser.parseInfix("5 - 3 + -1").eval());
        assertEquals(20, Parser.parseInfix("(2+3)*4").eval());
        assertEquals(0.001, Parser.parseInfix("1e-3").eval(), 1e-12);
    }

    @Test
    void infixReportsAllRequiredErrorsTest() {
        infixError("", "empty input");
        infixError("2 + a", "invalid token");
        infixError("2 +", "insufficient operands");
        infixError("2 + 3 4", "too many operands");
        infixError("( 2 + 3", "mismatched open paren");
        infixError("2 + 3 )", "mismatched close paren");
        infixError("()", "insufficient operands");
        infixError("2 (3)", "too many operands");
    }
}
