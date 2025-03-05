package compiler.Exceptions.Lexer;

public class NotASCIIException extends Exception {

  public NotASCIIException(String token, String line) {
    super("Syntax error: Non-ASCII character detected: " + "'" + token + "'" + " on line " + line);
  }
}
