package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class UnrecognisedToken extends Symbol {

  private int line_number;
  private String token;

  public UnrecognisedToken() {
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
