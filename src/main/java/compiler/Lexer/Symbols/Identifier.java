package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Identifier extends Symbol {

  private String attribute;
  private final String symbolName = "Identifier";


  public Identifier(String value) {
    attribute = value;
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
    return symbolName;
  }

  @Override
  public String toString() {
    return "<" + symbolName + "," + attribute + ">";
  }
}
