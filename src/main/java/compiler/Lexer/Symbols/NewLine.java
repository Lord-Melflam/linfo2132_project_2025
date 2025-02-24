package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class NewLine extends Symbol {

  private final String symbolName = "NewLine";

  public NewLine(String value) {
  }

  public NewLine() {
  }

  public boolean matches(String word) {
    return word.equals("\n");
  }

  @Override
  public String getName() {
    return symbolName;
  }

  @Override
  public String toString() {
    return "<" + symbolName + ">";
  }
}
