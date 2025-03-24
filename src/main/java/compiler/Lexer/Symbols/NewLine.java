package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class NewLine extends Symbol {

  private int line_number;
  private int column;

  public NewLine(int line_number, int column) {
    this.line_number = line_number;
    this.column = column;
  }


  public NewLine() {
  }

  public boolean matches(String word) {
    return word.equals("\n");
  }

  @Override
  public int getColumn() {
    return column;
  }

  public String getToken() {
    return "\\n";
  }

  @Override
  public String getName() {
    return NewLine.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public Symbol clone() {
    return new NewLine();
  }

  @Override
  public String toString() {
    return "<" + getName() + ">";
  }
}
