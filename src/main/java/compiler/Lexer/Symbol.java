package compiler.Lexer;

public abstract class Symbol {

  public Symbol() {
  }

  protected abstract boolean matches(String input);

  public abstract String getName();

  public abstract int getLine_number();

  public abstract String getToken();

  public static boolean isAscii(char ch) {
    return ch <= 127;
  }

  public abstract String toString();

}
