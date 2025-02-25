package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Comment extends Symbol {

  private String attribute;
  private final String symbolName = "Comment";
  private int line_number;

  public Comment(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Comment() {
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

}
