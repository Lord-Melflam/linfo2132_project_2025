package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Keyword extends Symbol {

  private int line_number;
  private static final ArrayList<String> KEYWORDS = new ArrayList<>(
      List.of("free", "final", "rec", "fun", "for", "while", "if", "else", "return", "of",
          "array"));
  private String attribute;
  private final String symbolName = "Keyword";

  public Keyword(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Keyword() {
  }

  public boolean matches(String word) {
    return KEYWORDS.contains(word);
  }

  @Override
  public String getName() {
    return Keyword.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
