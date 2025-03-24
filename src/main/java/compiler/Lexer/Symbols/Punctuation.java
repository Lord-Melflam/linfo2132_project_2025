package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Punctuation extends Symbol {

  private static final ArrayList<String> PUNCTUATIONS = new ArrayList<>(
      List.of(".", ";", ","));

  private String attribute;
  private int line_number;
  private int column;

  public Punctuation(String attribute, int line_number, int column) {
    this.attribute = attribute;
    this.line_number = line_number;
    this.column = column;
  }

  public Punctuation() {
  }

  public boolean matches(String word) {
    return PUNCTUATIONS.contains(word);
  }

  @Override
  public int getColumn() {
    return column;
  }

  @Override
  public String getName() {
    return Punctuation.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  public String getToken() {
    return attribute;
  }

  @Override
  public Symbol clone() {
    return new Punctuation(this.attribute, this.line_number, this.column);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
