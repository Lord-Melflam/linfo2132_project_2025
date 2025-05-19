package compiler.Exceptions.Semantic;

public class RecordError extends RuntimeException {

  public RecordError(String message) {
    super(message);
  }

  public RecordError(String line, String field) {
    super("Semantic exception: Record error : Invalid field '{" + field
        + "}' on line " + line + ".");
  }
}

