package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class SpecialSymbol extends Symbol {

  private static final ArrayList<String> SPECIAL_SYMBOLS = new ArrayList<>(
      List.of("(", ")", "{", "}", "[", "]"));
  private static final ArrayList<String> OPEN_SPECIAL_SYMBOLS = new ArrayList<>(
      List.of("(", "{", "["));
  private static final ArrayList<String> CLOSE_SPECIAL_SYMBOLS = new ArrayList<>(
      List.of(")", "}", "]"));
  private String attribute;
  private int line_number;
  private int column;

  public SpecialSymbol(String attribute, int line_number, int column) {
    this.attribute = attribute;
    this.line_number = line_number;
    this.column = column;
  }

  public SpecialSymbol() {
  }

  public boolean matches(String word) {
    return SPECIAL_SYMBOLS.contains(word);
  }

  @Override
  public int getColumn() {
    return column;
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
  public Symbol clone() {
    return new SpecialSymbol(this.attribute, this.line_number, this.column);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
