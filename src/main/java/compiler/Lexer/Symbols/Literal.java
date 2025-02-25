package compiler.Lexer.Symbols;

import compiler.Lexer.Symbol;
import java.util.ArrayList;
import java.util.List;

public class Literal extends Symbol {

  private static final ArrayList<String> BOOL = new ArrayList<>(
      List.of("true", "false"));
  private String attribute;
  private final String symbolName = "Literal";
  private int line_number;

  public Literal(String value, int line) {
    attribute = value;
    line_number = line;
  }

  public Literal() {

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
    return !escaped;
}




  @Override
  public String toString() {
    return "<" + symbolName + "," + attribute + ">";
  }
}
