package compiler.Exceptions.Semantic;

public class ArgumentError extends RuntimeException {

  public ArgumentError(String type1, String type2, String line) {
    super("Semantic exception: Argument error " + "'" + type1 + "' is not " + "'" + type2 + "'"
        + " on line " + line);
  }

  public ArgumentError(int line, int expected, int actual) {
    super(
        "Semantic exception: Argument error: incorrect number of arguments – expected {" + expected
            + "}, received {" + actual + "}"
            + " on line " + line);
  }

  public ArgumentError(String message) {
    super(message);
  }
}