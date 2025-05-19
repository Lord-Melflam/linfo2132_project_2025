package compiler.Exceptions.Semantic;

public class DuplicateDeclarationError extends RuntimeException {

  public DuplicateDeclarationError(String type, int line) {
    super("Semantic exception: Declaration error " + "'" + type + "' is already declared "
        + " on line " + line);
  }
}
