package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Assignment extends Symbol {

  private int line_number;
  private String attribute;
  private int column;

  public Assignment(String value, int line, int column) {
    attribute = value;
    line_number = line;
    this.column = column;
  }

  public Assignment() {
  }

  @Override
  public int getColumn() {
    return column;
  }

  public int getLine_number() {
    return line_number;
  }

  public String getToken() {
    return attribute;
  }

  public boolean matches(String word) {
    return word.equals("=");
  }

  @Override
  public String getName() {
    return Assignment.class.getSimpleName();
  }

  @Override
  public Symbol clone() {
    return new Assignment(this.attribute, this.line_number, this.column);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}