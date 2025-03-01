package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class SpecialSymbol extends Symbol {

  private static final ArrayList<String> SPECIAL_SYMBOLS = new ArrayList<>(
      List.of("(", ")", "{", "}", "[", "]", ".", ";", ","));
  private static final ArrayList<String> OPEN_SPECIAL_SYMBOLS = new ArrayList<>(
      List.of("(", "{", "["));
  private static final ArrayList<String> CLOSE_SPECIAL_SYMBOLS = new ArrayList<>(
      List.of(")", "}", "]"));
  private int line_number;

  private String attribute;

  public SpecialSymbol(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public SpecialSymbol() {
  }

  public boolean matches(String word) {
    return SPECIAL_SYMBOLS.contains(word);
  }

  @Override
  public String getName() {
    return SpecialSymbol.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  private boolean isOpen() {
    return OPEN_SPECIAL_SYMBOLS.contains(attribute);
  }

  private boolean isClose() {
    return CLOSE_SPECIAL_SYMBOLS.contains(attribute);
  }

  public String getToken() {
    return attribute;
  }

  public String IsOpenOrClose() {
    if (isClose()) {
      return "Close";
    }
    return isOpen() ? "Open" : null;
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
