package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Space extends Symbol {

  private final String symbolName = "Space";
  private int line_number;

  public Space(String value, int line) {
    line_number = line;
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

  public int getLine_number() {
    return line_number;
  }

  public String toString() {
    return "<" + symbolName + ">";
  }
}
