package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Comment extends Symbol {

  private String attribute;
  private int line_number;
  private int column;

  public Comment(String attribute, int line_number, int column) {
    this.attribute = attribute;
    this.line_number = line_number;
    this.column = column;
  }

  public Comment() {
  }

  @Override
  public int getColumn() {
    return column;
  }

  public String getToken() {
    return attribute;
  }

  @Override
  public boolean matches(String word) {
    return word.startsWith("$") && word.length() > 1 && isAscii(word.charAt(word.length() - 1))
        && word.charAt(word.length() - 1) != '\n';
  }

  @Override
  public String getName() {
    return Comment.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }

  @Override
  public Symbol clone() {
    return new Comment(this.attribute, this.line_number, this.column);
  }
}
