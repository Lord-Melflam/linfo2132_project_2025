package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class BuiltInFunction extends Symbol {

  private String attribute;
  private int line_number;
  private static final ArrayList<String> BUILTIN = new ArrayList<>(
      List.of("readInt", "readFloat", "readString", "writeInt", "writeFloat", "write", "writeln",
          "!", "len", "floor", "chr"
      ));

  public BuiltInFunction(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public BuiltInFunction() {
  }

  public String getToken() {
    return attribute;
  }

  public int getLine_number() {
    return line_number;
  }

  public boolean matches(String word) {
    return BUILTIN.contains(word);
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
