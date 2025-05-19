package compiler.Exceptions.Semantic;

public class ReturnError extends RuntimeException {

  public ReturnError(int line) {
    super("Semantic exception: Return error: Return statement is not correct on line " + line);
  }

}
