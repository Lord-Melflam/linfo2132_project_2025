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
  private final String symbolName = "SpecialSymbol";

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
    return symbolName;
  }

  public int getLine_number() {
    return line_number;
  }

  public boolean isOpen() {
    return OPEN_SPECIAL_SYMBOLS.contains(attribute);
  }

  public boolean isClose() {
    return CLOSE_SPECIAL_SYMBOLS.contains(attribute);
  }

  public String IsOpenOrClose() {
    if (isClose()) {
      return "Close";
    }
    return isOpen() ? "Open" : "Not open and close Special Symbol";
  }

  @Override
  public String toString() {
    return "<" + symbolName + "," + attribute + ">";
  }
}
