package compiler.Lexer;

import java.util.ArrayList;
import java.util.List;

public class FindSymbol {

  private final ArrayList<String> Keywords = new ArrayList<>(
      List.of("free", "final", "rec", "fun", "for", "while", "if", "else", "return"));
  private final ArrayList<String> bool = new ArrayList<>(
      List.of("true", "false"));
  private final ArrayList<String> special_symbols = new ArrayList<>(
      List.of("=", "+", "-", "*", "/", "%", "==", "!=", "<", ">", "<=", ">=", "(", ")",
          "{", "}", "[", "]", ".", "&&", "||", ";"));

  public FindSymbol() {

  }

  public String longestMatchSymbols(String word) {
    String longestCategory = null;

    if (Keywords.contains(word)) {
      longestCategory = "Keyword";
    }
    if (isComment(word)) {
      longestCategory = "Comment";
    }
    if (isType(word)) {
      longestCategory = "Type";
    }
    if (isSpecialSymbols(word)) {
      longestCategory = "SpecialSymbols";
    }
    if (isNewLine(word)) {
      longestCategory = "NewLine";
    }
    if (isIdentifier(word)) {
      longestCategory = "Identifier";
    }
    if (isTab(word)){
      longestCategory="Tab";
    }
    if (isSpace(word)){
      longestCategory="Space";
    }
    return longestCategory;
  }

  public boolean isAscii(char ch) {
    return ch <= 127;
  }

  public boolean isType(String ch) {
    return isInt(ch) || isFloat(ch) || isString(ch) || bool.contains(ch);
  }

  private boolean isString(String ch) {
    if (ch == null || ch.length() < 2) {
      return false;
    }
    if (ch.charAt(0) != '"' || ch.charAt(ch.length() - 1) != '"') {
      return false;
    }
    for (int i = 1; i < ch.length() - 1; i++) {
      if (ch.charAt(i) == '"' && ch.charAt(i - 1) != '\\') {
        return false;
      }
    }
    return true;
  }

  public boolean isComment(String ch) {
    return ch.startsWith("$") && ch.length() > 1 && isAscii(ch.charAt(ch.length() - 1))
        && ch.charAt(ch.length() - 1) != '\n';
  }

  public boolean isLetter(String ch) {
    char lastChar = ch.charAt(ch.length() - 1);
    return (lastChar >= 'A' && lastChar <= 'Z') || (lastChar >= 'a' && lastChar <= 'z');
  }

  public boolean isSTartLetter(String ch) {
    char firstChar = ch.charAt(0);
    return (firstChar >= 'A' && firstChar <= 'Z') || (firstChar >= 'a' && firstChar <= 'z');
  }

  public boolean isSpecialSymbols(String ch) {
    return special_symbols.contains(ch);
  }

  public boolean isNewLine(String ch) {
    return ch.equals("\n");
  }
  public boolean isSpace(String ch) {
    return !ch.isEmpty() && ch.charAt(0) == ' ';
  }

  public boolean isTab(String ch) {
    return !ch.isEmpty() && ch.charAt(0) == '\t';
  }


  public boolean isIdentifier(String ch) {
    return ch.length() >= 1 && (ch.charAt(0) == '_' || isSTartLetter(ch)) && isLetter(ch);
  }

  public boolean isInt(String ch) {
    if (ch == null || ch.isEmpty()) {
      return false;
    }
    int i = (ch.charAt(0) == '-') ? 1 : 0;
    for (; i < ch.length(); i++) {
      if (ch.charAt(i) < '0' || ch.charAt(i) > '9') {
        return false;
      }
    }
    return Integer.parseInt(ch)<Integer.MAX_VALUE && Integer.parseInt(ch)>Integer.MIN_VALUE;
  }

  public boolean isFloat(String ch) {
    if (ch == null || ch.isEmpty()) {
      return false;
    }
    boolean hasDot = false, hasDigit = false;
    int i = (ch.charAt(0) == '-') ? 1 : 0;
    for (; i < ch.length(); i++) {
      char c = ch.charAt(i);
      if (c == '.') {
        if (hasDot) return false;
        hasDot = true;
      } else if (c >= '0' && c <= '9') {
        hasDigit = true;
      } else {
        return false;
      }
    }
    return hasDot && hasDigit && Float.parseFloat(ch)<Float.MAX_VALUE && Integer.parseInt(ch)>Float.MIN_VALUE;
  }

  public ArrayList<String> symbols(String word) {
    ArrayList<String> set = new ArrayList<>();
    if (Keywords.contains(word)) {
      set.add("Keyword");
    } else if (isComment(word)) {
      set.add("Comment");
    } else if (isType(word)) {
      set.add("Type");
    } else if (isSpecialSymbols(word)) {
      set.add("SpecialSymbols");
    } else if (isNewLine(word)) {
      set.add("NewLine");
    } else if (isIdentifier(word)) {
      set.add("Identifier");
    }
    return set;
  }

  public String toString(String symbol, String word) {
    return "<" + symbol + "," + word + ">";
  }
}
