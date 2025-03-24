package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Tabulation extends Symbol {

  private int line_number;

  public Tabulation(String value, int line) {
    line_number = line;
  }

  public Tabulation() {
  }

  @Override
  public boolean matches(String word) {
    return word.equals("    ");
  }

  @Override
  public String getName() {
    return Tabulation.class.getSimpleName();
  }

  @Override
  public int getColumn() {
    return 0;
  }

  public String getToken() {
    return "  ";
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public Symbol clone() {
    return new Tabulation();
  }

  public String toString() {
    return "<" + getName() + ">";
  }
}
