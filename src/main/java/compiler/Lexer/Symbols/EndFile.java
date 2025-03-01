package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class EndFile extends Symbol {

  private int line_number;

  public EndFile() {
  }

  public EndFile(int line) {
    line_number = line;
  }

  @Override
  protected boolean matches(String input) {
    return false;
  }

  @Override
  public String getName() {
    return EndFile.class.getSimpleName();
  }

  @Override
  public int getLine_number() {
    return line_number;
  }

  @Override
  public String toString() {
    return "<" + getName() + ">";
  }
}
