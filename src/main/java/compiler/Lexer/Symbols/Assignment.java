package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Assignment extends Symbol {

  private String attribute;
  private final String symbolName = "Assignment";

  public Assignment(String value) {
    attribute = value;
  }

  public Assignment() {
  }

  public boolean matches(String word) {
    return word.equals("=");
  }

  @Override
  public String getName() {
    return symbolName;
  }

  @Override
  public String toString() {
    return "<" + symbolName + "," + attribute + ">";
  }
}