package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class TypeSpecifier extends Symbol {

  public static final ArrayList<String> TYPE_SPECIFIERS = new ArrayList<>(
      List.of("int", "float", "bool", "string"));
  private String attribute;
  private final String symbolName = "TypeSpecifier";
  private int line_number;


  public TypeSpecifier(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public TypeSpecifier() {

  }

  public boolean matches(String word) {
    for (String type : TYPE_SPECIFIERS) {
      String array = type + "[]";
      if (type.equals(word) || array.equals(word)) {
        return true;
      }
    }
    return false;
  }

  public int getLine_number() {
    return line_number;
  }

  public ArrayList<String> getTypeSpecifier() {
    return TYPE_SPECIFIERS;
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
