package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class UnrecognisedToken extends Symbol {

  private String token;
  private int line_number;
  private int column;

  public UnrecognisedToken() {
  }

  public UnrecognisedToken(String token, int line_number, int column) {
    this.token = token;
    this.line_number = line_number;
    this.column = column;
  }

  public UnrecognisedToken(int line, String token) {
    line_number = line;
    this.token = token;
  }

  @Override
  protected boolean matches(String input) {
    return false;
  }

  public String getName() {
    return UnrecognisedToken.class.getSimpleName();
  }

  @Override
  public int getColumn() {
    return column;
  }

  @Override
  public int getLine_number() {
    return line_number;
  }

  public String getToken() {
    return token;
  }

  @Override
  public Symbol clone() {
    return new UnrecognisedToken();
  }

  public String toString() {
    return "<" + getName() + ',' + token + ">";
  }
}
