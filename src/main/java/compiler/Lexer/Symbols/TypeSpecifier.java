package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class TypeSpecifier extends Symbol {

  private static final ArrayList<String> TYPE_SPECIFIERS = new ArrayList<>(
      List.of("int", "float", "bool", "string"));
  private String attribute;
  private int line_number;


  public TypeSpecifier(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public TypeSpecifier() {

  }

  public boolean matches(String word) {
    return TYPE_SPECIFIERS.contains(word) || isArrayTypeSpecifier(word) || isArrayRecord(word);
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
    for (String s : TypeSpecifier.TYPE_SPECIFIERS) {
      return s.equals(attribute) ? s : null;
    }
    return null;
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
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
