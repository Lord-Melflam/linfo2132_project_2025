package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class SpecialSymbol extends Symbol {

  private static final ArrayList<String> SPECIAL_SYMBOLS = new ArrayList<>(
      List.of("(", ")", "{", "}", "[", "]", ".", ";", ","));

  private String attribute;
  private final String symbolName = "SpecialSymbol";

  public SpecialSymbol(String value) {
    attribute = value;
  }

  public SpecialSymbol() {
  }

  public boolean matches(String word) {
    return SPECIAL_SYMBOLS.contains(word);
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
