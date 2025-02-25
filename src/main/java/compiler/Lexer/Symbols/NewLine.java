package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class NewLine extends Symbol {

  private int line_number;


  public NewLine(String value, int line) {
    line_number = line;
  }

  public NewLine() {
  }

  public boolean matches(String word) {
    return word.equals("\n");
  }

  @Override
  public String getName() {
    return NewLine.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public String toString() {
    return "<" + getName() + ">";
  }
}
