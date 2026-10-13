public record BinopNode(String operator, AST left, AST right) implements AST {
    @Override
    public double eval() {
        double a = left.eval();
        double b = right.eval();

        return switch(operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            case "^" -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("invalid token");
        };
    }
}