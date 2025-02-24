package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Operator extends Symbol {

  private static final ArrayList<String> OPERATORS = new ArrayList<>(
      List.of("+", "-", "*", "/", "%", "==", "!=", "<", ">", "<=", ">=", "&&", "||"));
  private String attribute;
  private final String symbolName = "Operator";

  public Operator(String value) {
    attribute = value;
  }

  public Operator() {
  }

  public boolean matches(String word) {
    return OPERATORS.contains(word);
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