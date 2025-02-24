package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Comment extends Symbol {

  private String attribute;
  private final String symbolName = "Comment";

  public Comment(String value) {
    attribute = value;
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
    return symbolName;
  }

  @Override
  public String toString() {
    return "<" + symbolName + "," + attribute + ">";
  }

}
