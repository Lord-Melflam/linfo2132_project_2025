package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Keyword extends Symbol {

  private int line_number;
  public static final ArrayList<String> KEYWORDS = new ArrayList<>(
      List.of("free", "final", "rec", "fun", "for", "while", "if", "else", "return", "of",
          "array", "break", "continue"));
  private String attribute;
  private int column;

  public Keyword(String attribute, int line_number, int column) {
    this.line_number = line_number;
    this.attribute = attribute;
    this.column = column;
  }


  public Keyword() {
  }

  @Override
  public int getColumn() {
    return column;
  }

  public String getToken() {
    return attribute;
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
  public Symbol clone() {
    return new Keyword(this.attribute, this.line_number, this.column);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
