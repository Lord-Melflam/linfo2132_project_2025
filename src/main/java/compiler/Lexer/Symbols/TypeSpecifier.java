package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class TypeSpecifier extends Symbol {

  private static final ArrayList<String> TYPE_SPECIFIERS = new ArrayList<>(
      List.of("int", "float", "bool", "string"));
  private String attribute;
  private int line_number;
  private int column;


  public TypeSpecifier(String attribute, int line_number, int column) {
    this.attribute = attribute;
    this.line_number = line_number;
    this.column = column;
  }

  public TypeSpecifier() {

  }

  public boolean matches(String word) {
    return TYPE_SPECIFIERS.contains(word) || isArrayTypeSpecifier(word) || isArrayRecord(word);
  }

  @Override
  public int getColumn() {
    return column;
  }

  public boolean endWithBracket(String word) {
    return (word.endsWith("[") || word.endsWith("]") || word.endsWith("[]"));
  }

  public boolean isArrayTypeSpecifier(String word) {
    for (String s : TypeSpecifier.TYPE_SPECIFIERS) {
      return word.startsWith(s) && endWithBracket(word);
    }
    return false;
  }

  public String typeOfTypeSpecifier() {
    if (TYPE_SPECIFIERS.contains(attribute)) {
      return attribute;
    }
    if (isArrayRecord(attribute)) {
      return "Record" + "_" + attribute.substring(0, attribute.length() - 2);
    }
    if (isArrayTypeSpecifier(attribute)) {
      return attribute.substring(0, attribute.length() - 2);
    }
    return null;
  }

  public String getToken() {
    return attribute;
  }

  public boolean isArrayRecord(String word) {
    if (endWithBracket(word)) {
      String potentialRecord = "";
      if (word.length() >= 2) {
        potentialRecord = word.substring(0, word.length() - 2);
      } else {
        return false;
      }
      return new Record().matches(potentialRecord);
    }
    return false;
  }

  public int getLine_number() {
    return line_number;
  }

  @Override
  public String getName() {
    return TypeSpecifier.class.getSimpleName();
  }

  @Override
  public Symbol clone() {
    return new TypeSpecifier(this.attribute, this.line_number, this.column);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
