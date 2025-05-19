package compiler.Exceptions.Semantic;

public class OperatorError extends Exception {

  public OperatorError(int line) {
    super(
        "Semantic exception: Operator error: Numeric type must be the same on line "
            + line);
  }

  public OperatorError(String message) {
    super(message);
  }
}
