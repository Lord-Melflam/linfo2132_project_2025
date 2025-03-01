package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Literal extends Symbol {

  private static final ArrayList<String> BOOL = new ArrayList<>(
      List.of("true", "false"));
  private String attribute;
  private int line_number;
  private String typeOfLiteral;

  public Literal(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Literal() {

  }

  public void setTypeOfLiteral(String typeOfLiteral) {
    this.typeOfLiteral = typeOfLiteral;
  }

  public boolean matches(String word) {
    if (word.equals("-")) {
      return false;
    }
    return isInt(word) || isFloat(word) || isString(word) || BOOL.contains(word) || isArray(word);
  }

  @Override
  public String getName() {
    return Literal.class.getSimpleName();
  }

  public int getLine_number() {
    return line_number;
  }

  public boolean isArray(String word) {
    if (word.startsWith("[") && word.endsWith("]")) {
      setTypeOfLiteral("array");
      return true;
    } else {
      return false;
    }
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
    if (Integer.parseInt(word) < Integer.MAX_VALUE && Integer.parseInt(word) > Integer.MIN_VALUE) {
      setTypeOfLiteral("int");
      return true;
    } else {
      return false;
    }
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
    if (hasDot && hasDigit && Float.parseFloat(word) < Float.MAX_VALUE
        && Float.parseFloat(word) > Float.MIN_VALUE) {
      setTypeOfLiteral("float");
      return true;
    } else {
      return false;
    }
  }

  private boolean isString(String word) {
    if (word == null || word.length() < 2) {
      return false;
    }
    if (word.charAt(0) != '"' || word.charAt(word.length() - 1) != '"') {
      return false;
    }

    boolean escaped = false;
    for (int i = 1; i < word.length() - 1; i++) {
      char c = word.charAt(i);

      if (escaped) {
        // Only allow valid escape sequences
        if (c != '"' && c != '\\' && c != 'n') {
          return false;
        }
        escaped = false; // Reset escape state
      } else {
        if (c == '\\') {
          escaped = true; // Start escape sequence
        } else if (c == '"') {
          return false; // Found an unescaped quote before the final one
        }
      }
    }

    // The final quote is always valid
    if (!escaped) {
      setTypeOfLiteral("string");
      return true;
    } else {
      return false;
    }
  }

  public String typeOfLiteral() {
    return typeOfLiteral;
  }


  @Override
  public String toString() {
    return "<" + getName() + "," + attribute + ">";
  }
}
