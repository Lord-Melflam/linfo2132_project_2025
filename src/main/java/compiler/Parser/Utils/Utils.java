package compiler.Parser.Utils;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Symbol;
import compiler.Parser.Parser;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.Observer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Utils implements Observer {

  private Symbol currentSymbol;
  private Symbol previousSymbol;
  Position CurrentPosition;
  private Parser parser;
  private ArrayList<Symbol> symbols = new ArrayList<>();
  private LinkedList<Symbol> allSymbols;
  private int index;

  public Utils() {

  }

  public Utils(Parser parser, LinkedList<Symbol> allSymbols) {
    this.parser = parser;
    this.allSymbols = allSymbols;
  }

  public Symbol getSymbol(int index) {
    return allSymbols.get(index);
  }
/*
  public boolean matchIndex(int savedIndex, String tokenName) throws ParserException {
    Symbol current = getSymbol(savedIndex);
    return checkTokenType(current, tokenName);
  }*/

  public boolean matchIndex(int savedIndex, TokenType tokenType) throws ParserException {
    Symbol current = getSymbol(savedIndex);
    String attribute = tokenType.getValue();
    if (attribute == null || attribute.equals("")) {
      return checkTokenType(tokenType.getCategory(), current.getName());
    }
    return checkTokenType(tokenType.getCategory(), current.getName()) && checkTokenType(attribute,
        current.getToken());
    /*Symbol symbol = allSymbols.get(savedIndex);
    throw new ParserException(symbol.getToken(), Integer.toString(symbol.getLine_number()));*/
  }


  private boolean checkTokenType(String type, String tokenName) {
    return type.equals(tokenName);
  }


  public int getIndex() {
    return index;
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

  public boolean lookahead_matches(int savedIndex, List<HashSet<TokenType>> expectedSymbols)
      throws UnrecognisedTokenException, ParserException {
    int counter = 0;
    for (HashSet<TokenType> expectedSymbol : expectedSymbols) {
      for (TokenType tokenType : expectedSymbol) {
        if (matchIndex(savedIndex, tokenType)) {
          counter++;
          savedIndex++;
          break;
        }
      }
      if (expectedSymbols.size() == counter) {
        return true;
      }
      if (counter == 0) {
        return false;
      }

    }
    return false;
  }

  @Override
  public void update(Position CurrentPosition) {
    this.CurrentPosition = CurrentPosition;
  }
}
