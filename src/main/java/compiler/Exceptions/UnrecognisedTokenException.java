package compiler.Exceptions;

public class UnrecognisedTokenException extends Exception {

  public UnrecognisedTokenException(String token, String line) {
    super("Syntax error: Unrecognized token " + "'" + token + "'" + " on line " + line);
  }
}