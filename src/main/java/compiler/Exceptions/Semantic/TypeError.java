package compiler.Exceptions.Semantic;

public class TypeError extends RuntimeException {

  public TypeError(String type1, String type2, String line) {
    super("Semantic exception: Type error " + "'" + type1 + "' is not " + "'" + type2 + "'"
        + " on line " + line);
  }

  public TypeError(int line) {
    super("Semantic exception: Type error " + "on line " + line);
  }

  public TypeError(int line, String expected, String actual) {
    super(
        "Semantic exception: Type error: expected argument of type – expected {" + expected
            + "}, received {" + actual + "}"
            + " on line " + line);
  }
}
