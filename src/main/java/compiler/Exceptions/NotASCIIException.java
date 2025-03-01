package compiler.Exceptions;

public class NotASCIIException extends Exception {

  public NotASCIIException(String token, String line) {
    super("Syntax error: Not an ascii token " + "'" + token + "'" + " on line " + line);
  }
}
