package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class BuiltInFunction extends Symbol {

  private String attribute;
  private final String symbolName = "BuiltInFunction";
  private int line_number;
  private static final ArrayList<String> BUILTIN = new ArrayList<>(
      List.of("readInt", "readFloat", "readString", "writeInt", "writeFloat", "write", "writeln"
      ));

  public BuiltInFunction(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public BuiltInFunction() {
  }

  public int getLine_number() {
    return line_number;
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
    return BuiltInFunction.class.getSimpleName();
  }


  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
