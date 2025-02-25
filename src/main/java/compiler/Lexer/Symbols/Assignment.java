package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Assignment extends Symbol {

  private int line_number;
  private String attribute;
  private final String symbolName = "Assignment";

  public Assignment(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Assignment() {
  }

  public int getLine_number() {
    return line_number;
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