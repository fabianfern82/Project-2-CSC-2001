import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final Pattern NUMBER = Pattern.compile(
            "(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][+-]?\\d+)?");

    public static AST parsePostfix(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        Deque<AST> expressions = new ArrayDeque<>();
        for (String token : input.trim().split("\\s+")) {
            if (isOperator(token)) {
                apply(token, expressions);
            } else {
                try {
                    expressions.push(new NumNode(Double.parseDouble(token)));
                } catch (NumberFormatException exception) {
                    throw new IllegalArgumentException("invalid token");
                }
            }
        }
        if (expressions.size() != 1) {
            throw new IllegalArgumentException("too many operands");
        }
        return expressions.pop();
    }

    public static AST parseInfix(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("empty input");
        }

        Deque<AST> expressions = new ArrayDeque<>();
        ArrayStack operators = ArrayStack.empty_stack();
        boolean expectOperand = true;
        int openParens = 0;

        for (int i = 0; i < input.length();) {
            char ch = input.charAt(i);
            if (Character.isWhitespace(ch)) {
                i++;
                continue;
            }

            int signLength = expectOperand && (ch == '-' || ch == '+') ? 1 : 0;
            Matcher number = NUMBER.matcher(input);
            number.region(i + signLength, input.length());
            if (number.lookingAt()) {
                if (!expectOperand) {
                    throw new IllegalArgumentException("too many operands");
                }
                expressions.push(new NumNode(Double.parseDouble(input.substring(i, number.end()))));
                i = number.end();
                expectOperand = false;
                continue;
            }

            if (ch == '(') {
                if (!expectOperand) {
                    throw new IllegalArgumentException("too many operands");
                }
                operators.push("(");
                openParens++;
                i++;
            } else if (ch == ')') {
                if (openParens == 0) {
                    throw new IllegalArgumentException("mismatched close paren");
                }
                if (expectOperand) {
                    throw new IllegalArgumentException("insufficient operands");
                }
                while (!operators.peek().equals("(")) {
                    apply(operators.pop(), expressions);
                }
                operators.pop();
                openParens--;
                expectOperand = false;
                i++;
            } else if (isOperator(String.valueOf(ch))) {
                if (expectOperand) {
                    throw new IllegalArgumentException("insufficient operands");
                }
                String current = String.valueOf(ch);
                while (!operators.is_empty() && isOperator(operators.peek())
                        && (precedence(operators.peek()) > precedence(current)
                        || (precedence(operators.peek()) == precedence(current)
                        && !current.equals("^")))) {
                    apply(operators.pop(), expressions);
                }
                operators.push(current);
                expectOperand = true;
                i++;
            } else {
                throw new IllegalArgumentException("invalid token");
            }
        }

        if (openParens != 0) {
            throw new IllegalArgumentException("mismatched open paren");
        }
        if (expectOperand) {
            throw new IllegalArgumentException("insufficient operands");
        }
        while (!operators.is_empty()) {
            apply(operators.pop(), expressions);
        }
        return expressions.pop();
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^");
    }

    private static void apply(String operator, Deque<AST> expressions) {
        if (expressions.size() < 2) {
            throw new IllegalArgumentException("insufficient operands");
        }
        AST right = expressions.pop();
        AST left = expressions.pop();
        expressions.push(new BinopNode(operator, left, right));
    }

    private static int precedence(String operator) {
        if (operator.equals("^")) {
            return 3;
        }
        if (operator.equals("*") || operator.equals("/")) {
            return 2;
        }
        return 1;
    }
}
