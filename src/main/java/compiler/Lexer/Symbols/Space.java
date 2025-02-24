package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Space extends Symbol {

  private final String symbolName = "Space";

  public Space(String value) {
  }

  public Space() {
  }

  @Override
  public boolean matches(String word) {
    return !word.isEmpty() && word.charAt(0) == ' ';
  }

  @Override
  public String getName() {
    return symbolName;
  }

  public String toString() {
    return "<" + symbolName + ">";
  }
}
