package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Space extends Symbol {

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

  public String getToken() {
    return " ";
  }

  @Override
  public String getName() {
    return Space.class.getSimpleName();
  }

  @Override
  public int getColumn() {
    return 0;
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public Symbol clone() {
    return new Space();
  }

  public String toString() {
    return "<" + getName() + ">";
  }
}
