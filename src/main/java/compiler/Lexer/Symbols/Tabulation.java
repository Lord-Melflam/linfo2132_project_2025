package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Tabulation extends Symbol {

  private final String symbolName = "Tabulation";

  public Tabulation(String value) {
  }

  public Tabulation() {
  }

  @Override
  public boolean matches(String word) {
    return word.equals("    ");
  }

  @Override
  public String getName() {
    return symbolName;
  }

  public String toString() {
    return "<" + symbolName + ">";
  }
}
