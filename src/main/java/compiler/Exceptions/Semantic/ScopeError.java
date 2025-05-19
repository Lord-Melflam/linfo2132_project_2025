package compiler.Exceptions.Semantic;

public class ScopeError extends RuntimeException {

  public ScopeError(String element, int line) {
    super(
        "Semantic exception: Scope error: the requested element {" + element
            + "} is not accessible in the current context"
            + " on line " + line);
  }

  public ScopeError(String message) {
    super(message);
  }
}