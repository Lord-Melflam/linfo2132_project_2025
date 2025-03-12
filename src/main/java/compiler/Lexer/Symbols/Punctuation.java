package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Punctuation extends Symbol {

  private static final ArrayList<String> PUNCTUATIONS = new ArrayList<>(
      List.of(".", ";", ","));
  private int line_number;

  private String attribute;

  public Punctuation(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Punctuation() {
  }

  public boolean matches(String word) {
    return PUNCTUATIONS.contains(word);
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
    return new Punctuation(this.attribute, this.line_number);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
