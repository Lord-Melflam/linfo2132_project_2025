package compiler.Exceptions.Semantic;

public class MissingConditionError extends RuntimeException {

  public MissingConditionError(int line) {
    super(
        "Semantic exception: Missing condition error: Condition must be a boolean on line " + line);
  }
}
