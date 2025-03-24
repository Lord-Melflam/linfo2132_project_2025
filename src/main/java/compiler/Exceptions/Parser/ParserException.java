package compiler.Exceptions.Parser;

public class ParserException extends Exception {

  public ParserException(String token, String line) {
    super("Syntax error: Unexpected token " + "'" + token + "'" + " on line " + line);
  }

  public ParserException(String line) {
    super("Syntax error: Expected token " + "'" + ";" + "'" + " on line " + line);
  }
}
