package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Operator extends Symbol {

  private static final ArrayList<String> OPERATORS = new ArrayList<>(
      List.of("+", "-", "*", "/", "%", "==", "!=", "<", ">", "<=", ">=", "&&", "||"));
  private String attribute;
  private int line_number;
  private int column;

  public Operator(String attribute, int line_number, int column) {
    this.attribute = attribute;
    this.line_number = line_number;
    this.column = column;
  }

  public Operator() {
  }

  public String getToken() {
    return attribute;
  }

  @Override
  public int getColumn() {
    return column;
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
  public Symbol clone() {
    return new Operator(this.attribute, this.line_number, this.column);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}