package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;

public class Generic extends Symbol {

  private String name;
  private String attribute;

  public Generic(String name, String value) {
    attribute = value;
    this.name = name;
  }

  public Generic() {
  }

  public String getToken() {
    return attribute;
  }

  public boolean matches(String word) {
    return false;
  }

  @Override
  public String getName() {
    return name;
  }

  public int getLine_number() {
    return -1;
  }

  @Override
  public Symbol clone() {
    return new Generic(this.name, this.attribute);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }

}
