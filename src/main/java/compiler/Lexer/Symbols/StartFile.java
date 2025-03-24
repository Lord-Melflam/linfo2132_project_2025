package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class StartFile extends Symbol {

  public StartFile() {
  }

  @Override
  protected boolean matches(String input) {
    return false;
  }

  public String getName() {
    return StartFile.class.getSimpleName();
  }

  @Override
  public int getColumn() {
    return 0;
  }

  @Override
  public int getLine_number() {
    return 1;
  }

  public String getToken() {
    return null;
  }

  @Override
  public Symbol clone() {
    return new StartFile();
  }

  public String toString() {
    return "<" + getName() + ">";
  }
}
