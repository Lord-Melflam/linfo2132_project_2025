package compiler.Parser;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.AST.RootNode;
import compiler.Parser.Grammar.Program.Program;
import compiler.Parser.Utils.ASTNodeProcessor;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Interface.Observer;
import compiler.Parser.Utils.Interface.Subject;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser implements Subject {

  private final List<Observer> observers = new ArrayList<>();
  private final Lexer lexer;
  private Symbol lookahead;
  private final Program program;
  private final ASTNodeProcessor nodeProcessor;
  private final LinkedList<Symbol> allSymbols;
  private int currentPosition;
  Position savedPosition;

  public Parser(Lexer lexer) throws UnrecognisedTokenException, ParserException {
    currentPosition = 0;
    savedPosition = new Position();
    this.lexer = lexer;
    allSymbols = lexer.getSymbols();
    Utils utils = new Utils(this, allSymbols);
    this.addObserver(utils);
    savedPosition.addObserver(utils);
    this.lookahead = lexer.getNextSymbol();
    program = new Program(utils);
    nodeProcessor = new ASTNodeProcessor();
    getAST();
  }

  public void getAST() throws UnrecognisedTokenException, ParserException {
    while (!allSymbols.get(savedPosition.getSavedPosition()).getName().equals("EndFile")) {
      for (int i = 0; i < 1; i++) {
        if (checkNode(program.isProgram(savedPosition))) {
          currentPosition = savedPosition.getSavedPosition();
          break;
        }
        savedPosition.setSavedPosition(currentPosition);
        if (checkNode(program.isRecord(savedPosition))) {
          currentPosition = savedPosition.getSavedPosition();
          break;
        }
        savedPosition.setSavedPosition(currentPosition);

        if (checkNode(program.isFunction(savedPosition))) {
          currentPosition = savedPosition.getSavedPosition();
          break;
        }
        savedPosition.setSavedPosition(currentPosition);

        if (checkNode(program.isStatement(savedPosition))) {
          currentPosition = savedPosition.getSavedPosition();
          break;
        }



       /* Symbol incorrectSymbol = getLookahead();
        throw new ParserException(incorrectSymbol.getToken(),
            Integer.toString(incorrectSymbol.getLine_number()));*/
      }

    }
  }

  public boolean checkNode(ASTNode astNode)
      throws ParserException, UnrecognisedTokenException {
    if (astNode != null) {
      astNode.accept(nodeProcessor);
      return true;
    }
    return false;
  }

  public RootNode getRootNode() {
    return nodeProcessor.getRoot();
  }


  public Symbol nextSymbol() throws UnrecognisedTokenException {
    this.lookahead = lexer.getNextSymbol();
    notifyObservers();
    return lookahead;
  }

  public Symbol getLookahead() {
    return lookahead;
  }

  @Override
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    for (Observer observer : observers) {
      observer.update(savedPosition);
    }
  }
}
