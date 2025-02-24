package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class BuiltInFunction extends Symbol {

  private String attribute;
  private final String symbolName = "BuiltInFunction";
  private static final ArrayList<String> BUILTIN = new ArrayList<>(
      List.of("readInt", "readFloat", "readString", "writeInt", "writeFloat", "write", "writeln"
      ));

  public BuiltInFunction(String value) {
    attribute = value;
  }

  public BuiltInFunction() {
  }

  public boolean matches(String word) {
    return negateBool(word) || charToString(word) || len(word) || floor(word) || BUILTIN.contains(
        word);
  }

  public boolean negateBool(String word) {
    return word.startsWith("!(") && word.endsWith(")");
  }

  public boolean charToString(String word) {
    return word.startsWith("chr(") && word.endsWith(")");
  }

  public boolean len(String word) {
    return word.startsWith("len(") && word.endsWith(")");
  }

  public boolean floor(String word) {
    return word.startsWith("floor(") && word.endsWith(")");
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
