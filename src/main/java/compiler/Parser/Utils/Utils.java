package compiler.Parser.Utils;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Symbol;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Interfaces.Observer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Utils implements Observer {

  private Position currentPosition;
  private Position currentPositionClone;
  private LinkedList<Symbol> allSymbols;
  private ArrayList<ASTNode> astNodes;
  private GenericNode<String> genericNode;

  public Utils() {
  }

  public Utils(LinkedList<Symbol> allSymbols) {
    this.allSymbols = allSymbols;
  }

  public Symbol getSymbol(int index) {
    if (index < 0 || index >= allSymbols.size()) {
      return null;
    }
    return allSymbols.get(index);
  }

  public boolean matchIndex(TokenType tokenType, boolean add)
      throws ParserException, UnrecognisedTokenException {
    Symbol current = getSymbol(currentPositionClone.getSavedPosition());
    if (current == null) {
      return false;
    }
    if (tokenType.getValue() == null || tokenType.getValue().isEmpty()) {
      if (tokenType.getCategory().equals(current.getName())) {
        incrementPosition(add);
        createNode(current);
        currentPositionClone = new Position(currentPosition.getSavedPosition());
        return true;
      }
      currentPositionClone = new Position(currentPosition.getSavedPosition());
    } else if (tokenType.getCategory().equals(current.getName()) && tokenType.getValue()
        .equals(current.getToken())) {
      incrementPosition(add);
      createNode(current);
      currentPositionClone = new Position(currentPosition.getSavedPosition());
      return true;
    }
    currentPositionClone = new Position(currentPosition.getSavedPosition());
    return false;
  }

  public boolean matchIndexList(TokenType tokenType, boolean add) {
    Symbol current = getSymbol(currentPositionClone.getSavedPosition());

    if (tokenType.getValue() == null || tokenType.getValue().isEmpty()) {
      if (tokenType.getCategory().equals(current.getName())) {
        incrementPosition(add);
        createNode(current);
        return true;
      }
    } else if (tokenType.getCategory().equals(current.getName()) && tokenType.getValue()
        .equals(current.getToken())) {
      incrementPosition(add);
      createNode(current);
      return true;
    }
    currentPositionClone = new Position(currentPosition.getSavedPosition());
    return false;
  }

  private void incrementPosition(boolean add) {
    currentPositionClone.add();
    if (add) {
      commitChanges();
    }
  }

  private void commitChanges() {
    currentPosition.setSavedPosition(currentPositionClone.getSavedPosition());
  }

  public boolean lookahead_matches(List<HashSet<TokenType>> expectedSymbols, boolean add) {
    int matchedCount = 0;
    astNodes = new ArrayList<>();
    Position originalPosition = new Position(currentPosition.getSavedPosition());

    for (HashSet<TokenType> expectedSymbol : expectedSymbols) {
      boolean matched = false;
      for (TokenType tokenType : expectedSymbol) {
        if (matchIndexList(tokenType, false)) {
          matched = true;
          break;
        }
      }
      if (!matched) {
        currentPositionClone = new Position(originalPosition.getSavedPosition());
        return false;
      }
      matchedCount++;
    }

    if (matchedCount == expectedSymbols.size() && add) {
      commitChanges();
    }
    return matchedCount == expectedSymbols.size();
  }


  public ArrayList<ASTNode> getAstNodes() {
    return astNodes;
  }

  public GenericNode<String> getGenericNode() {
    return genericNode;
  }

  private void createNode(Symbol currentSymbol) {
    genericNode = new GenericNode<>(currentSymbol.getName(), currentSymbol.getToken(),
        currentSymbol.getLine_number());
    if (astNodes != null) {
      astNodes.add(genericNode);
    }
  }

  public int getLine() {
    Symbol symbol = getSymbol(currentPosition.getSavedPosition());
    return symbol.getLine_number();
  }

  public void throwParserException() throws ParserException {
    Symbol symbol = getSymbol(currentPosition.getSavedPosition());
    throw new ParserException(symbol.getToken(), Integer.toString(symbol.getLine_number()));
  }

  @Override
  public void updatePosition(Position currentPosition) {
    this.currentPosition = currentPosition;
    this.currentPositionClone = new Position(currentPosition.getSavedPosition());
  }

}
