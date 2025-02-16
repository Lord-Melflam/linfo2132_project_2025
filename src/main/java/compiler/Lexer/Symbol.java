package compiler.Lexer;

public class Symbol {
  private final String token;
  private final String attribute;
  public Symbol(String symbol,String value) {
    this.token =symbol;
    this.attribute =value;
    System.out.println(toString());
  }
  public String toString() {
    if(token.equals("NewLine")){
      return "<" + token + ">";
    }
    return "<" + token + "," + attribute + ">";
  }
}
