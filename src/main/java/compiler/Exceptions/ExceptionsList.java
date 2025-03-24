package compiler.Exceptions;

import java.util.LinkedList;
import java.util.List;

public class ExceptionsList {

  private final LinkedList<Exception> exceptions;

  public ExceptionsList() {
    this.exceptions = new LinkedList<>();
  }

  public void addException(Exception exception) {
    if (exception != null) {
      exceptions.add(exception);
    }
  }

  public Exception getFirstException() {
    return exceptions.getFirst();
  }

  public List<Exception> getExceptions() {
    return exceptions;
  }
}
