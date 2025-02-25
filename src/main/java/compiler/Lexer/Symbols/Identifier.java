package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Identifier extends Symbol {

  private String attribute;
  private final String symbolName = "Identifier";
  private int line_number;


  public Identifier(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Identifier() {
  }

  public boolean isLetter(String word) {
    char lastChar = word.charAt(word.length() - 1);
    return (lastChar >= 'A' && lastChar <= 'Z') || (lastChar >= 'a' && lastChar <= 'z');
  }

  public boolean isSTartLetter(String word) {
    char firstChar = word.charAt(0);
    return (firstChar >= 'a' && firstChar <= 'z');
  }

  @Override
  public boolean matches(String word) {
    return word.length() >= 1 && (word.charAt(0) == '_' || isSTartLetter(word)) && isLetter(word);
  }

  @Override
  public String getName() {
    return Identifier.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
