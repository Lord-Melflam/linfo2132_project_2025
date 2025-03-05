package compiler.Parser.Utils;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Symbol;
import compiler.Parser.Parser;
import compiler.Parser.Utils.Interface.Observer;

public class Utils implements Observer {

  private Symbol currentSymbol;
  private Symbol previousSymbol;

  private Parser parser;

  public Utils() {
  }

  public Utils(Parser parser) {
    this.parser = parser;
  }

  @Override
  public void update(Symbol currentSymbol) {
    this.currentSymbol = currentSymbol;
  }

  public Symbol getCurrentSymbol() {
    return currentSymbol;
  }

  public Symbol getPreviousSymbol() {
    if (previousSymbol == null) {
      return null;
    }
    return previousSymbol;
  }

  public void match(String tokenName) throws UnrecognisedTokenException, ParserException {
    if (currentSymbol.getName().equals(tokenName)) {
      previousSymbol = getCurrentSymbol();
      parser.nextSymbol();
      return;
    }
    throw new ParserException(currentSymbol.getToken(),
        Integer.toString(currentSymbol.getLine_number()));
  }

  public boolean lookahead_is(String tokenType) {
    return currentSymbol.getName().equals(tokenType);
  }

  private void display() {
    System.out.println(currentSymbol.toString() + "ghhg");
  }
}
