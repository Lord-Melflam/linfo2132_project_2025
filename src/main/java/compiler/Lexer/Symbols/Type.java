package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Type extends Symbol {

  private static final ArrayList<String> BOOL = new ArrayList<>(
      List.of("true", "false"));
  private String attribute;
  private final String symbolName = "Type";
  private int line_number;

  public Type(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Type() {

  }

  public boolean matches(String word) {
    if (word.equals("-")) {
      return false;
    }
    return isInt(word) || isFloat(word) || isString(word) || BOOL.contains(word) || isArray(word);
  }

  @Override
  public String getName() {
    return symbolName;
  }

  public int getLine_number() {
    return line_number;
  }

  public boolean isArray(String word) {
    return word.startsWith("[") && word.endsWith("]");
  }

  public boolean isInt(String word) {
    if (word == null || word.isEmpty()) {
      return false;
    }
    int i = (word.charAt(0) == '-') ? 1 : 0;
    for (; i < word.length(); i++) {
      if (word.charAt(i) < '0' || word.charAt(i) > '9') {
        return false;
      }
    }
    return Integer.parseInt(word) < Integer.MAX_VALUE && Integer.parseInt(word) > Integer.MIN_VALUE;
  }

  public boolean isFloat(String word) {
    if (word == null || word.isEmpty()) {
      return false;
    }
    boolean hasDot = false, hasDigit = false;
    int i = (word.charAt(0) == '-') ? 1 : 0;
    for (; i < word.length(); i++) {
      char c = word.charAt(i);
      if (c == '.') {
        if (hasDot) {
          return false;
        }
        hasDot = true;
      } else if (c >= '0' && c <= '9') {
        hasDigit = true;
      } else {
        return false;
      }
    }
    return hasDot && hasDigit && Float.parseFloat(word) < Float.MAX_VALUE
        && Float.parseFloat(word) > Float.MIN_VALUE;
  }

  private boolean isString(String word) {
    if (word == null || word.length() < 2) {
      return false;
    }
    if (word.charAt(0) != '"' || word.charAt(word.length() - 1) != '"') {
      return false;
    }
    for (int i = 1; i < word.length() - 1; i++) {
      if (word.charAt(i) == '"' && word.charAt(i - 1) != '\\') {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return "<" + symbolName + "," + attribute + ">";
  }
}
