package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Operator extends Symbol {

  private int line_number;
  private static final ArrayList<String> OPERATORS = new ArrayList<>(
      List.of("+", "-", "*", "/", "%", "==", "!=", "<", ">", "<=", ">=", "&&", "||"));
  private String attribute;

  public Operator(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Operator() {
  }

  public boolean matches(String word) {
    return OPERATORS.contains(word);
  }

  @Override
  public String getName() {
    return Operator.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}