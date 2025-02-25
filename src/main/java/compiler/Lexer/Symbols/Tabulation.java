package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Tabulation extends Symbol {

  private final String symbolName = "Tabulation";
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
    return getClass().getName();
  }

  public int getLine_number() {
    return line_number;
  }

  public String toString() {
    return "<" + symbolName + ">";
  }
}
