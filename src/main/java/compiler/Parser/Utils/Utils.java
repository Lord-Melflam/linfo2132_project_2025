package compiler.Parser.Utils;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Symbol;
import compiler.Parser.AST.GenericNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.Observer;
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
      throw new IndexOutOfBoundsException("Invalid symbol index: " + index);
    }
    return allSymbols.get(index);
  }

  public boolean matchIndex(TokenType tokenType, boolean add)
      throws ParserException, UnrecognisedTokenException {
    Symbol current = getSymbol(currentPosition.getSavedPosition());

    if (tokenType.getValue() == null || tokenType.getValue().isEmpty()) {
      if (tokenType.getCategory().equals(current.getName())) {
        incrementPosition(add);
        createNode(current);
        return true;
      }
    } else if (tokenType.getCategory().equals(current.getName()) &&
        tokenType.getValue().equals(current.getToken())) {
      incrementPosition(add);
      createNode(current);
      return true;
    }

    return false;
  }

  private void incrementPosition(boolean add) {
    if (add) {
      currentPosition.add();
    }
  }

  /*private void incrementPosition(boolean add) {
    currentPositionClone.add();
   *//* if (add) {
      commitChanges();
    }*//*
  }*/

  private void commitChanges() {
    currentPosition.setSavedPosition(currentPositionClone.getSavedPosition());
  }

  public ArrayList<ASTNode> getAstNodes() {
    return astNodes;
  }

  public GenericNode<String> getGenericNode() {
    return genericNode;
  }

  /*private boolean resMatchIndex(TokenType tokenType, boolean add,boolean commit){
      return matchIndex()
  }*/
  public boolean lookahead_matches(List<HashSet<TokenType>> expectedSymbols,
      boolean add)
      throws UnrecognisedTokenException, ParserException {
    int counter = 0;
    astNodes = new ArrayList<>();
    for (HashSet<TokenType> expectedSymbol : expectedSymbols) {
      for (TokenType tokenType : expectedSymbol) {
        if (matchIndex(tokenType, add)) {
          counter++;
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

  private void createNode(Symbol currentSymbol) {
    genericNode = new GenericNode<>(currentSymbol.getName() + "Node",
        currentSymbol.getToken());
    if (astNodes != null) {
      astNodes.add(genericNode);
    }
  }


  @Override
  public void updatePosition(Position currentPosition) {
    this.currentPosition = currentPosition;
    this.currentPositionClone = new Position(currentPosition.getSavedPosition());
  }

  @Override
  public void updateSymbol(Symbol currentSymbol) {
  }
}
