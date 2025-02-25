package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Record extends Symbol {

  private String attribute;
  private final String symbolName = "Record";
  private int line_number;


  public Record(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Record() {
  }

  public boolean isLetter(String word) {
    char lastChar = word.charAt(word.length() - 1);
    return (lastChar >= 'A' && lastChar <= 'Z') || (lastChar >= 'a' && lastChar <= 'z');
  }

  public boolean isSTartLetter(String word) {
    char firstChar = word.charAt(0);
    return (firstChar >= 'A' && firstChar <= 'Z');
  }

  @Override
  public boolean matches(String word) {
    return word.length() >= 1 && (word.charAt(0) == '_' || isSTartLetter(word)) && isLetter(word);
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public String getName() {
    return Record.class.getSimpleName();
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
