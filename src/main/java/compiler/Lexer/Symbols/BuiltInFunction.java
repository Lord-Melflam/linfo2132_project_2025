package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class BuiltInFunction extends Symbol {

  private String attribute;
  private int line_number;
  private int column;

  private static final ArrayList<String> BUILTIN = new ArrayList<>(
      List.of("readInt", "readFloat", "readString", "writeInt", "writeFloat", "write", "writeln",
          "!", "len", "floor", "chr", "min", "max", "printType"
      ));

  public BuiltInFunction(String attribute, int line_number, int column) {
    this.attribute = attribute;
    this.line_number = line_number;
    this.column = column;
  }

  public BuiltInFunction() {
  }

  @Override
  public int getColumn() {
    return column;
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
  public Symbol clone() {
    return new BuiltInFunction(this.attribute, this.line_number, this.column);
  }

  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
