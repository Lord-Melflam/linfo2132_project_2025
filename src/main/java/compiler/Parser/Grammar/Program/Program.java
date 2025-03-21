package compiler.Parser.Grammar.Program;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.ASTNode.ASTNodeProcessor;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Declaration.Declaration;
import compiler.Parser.Grammar.Function.Function;
import compiler.Parser.Grammar.Record.Record;
import compiler.Parser.Grammar.Statement.StatementList;
import compiler.Parser.Utils.Interfaces.Observer;
import compiler.Parser.Utils.Interfaces.Subject;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Program implements Subject {

  private int currentPosition;

  private Utils utils;
  private final Lexer lexer;
  private final List<Observer> observers = new ArrayList<>();
  private Symbol lookahead;
  private LinkedList<Symbol> allSymbols;
  Position savedPosition;

  public Program(Lexer lexer) {
    currentPosition = 0;
    this.lexer = lexer;
    savedPosition = new Position();
  }

  public void setAllSymbols(LinkedList<Symbol> allSymbols) {
    this.allSymbols = allSymbols;
  }

  public void setUtils(Utils utils) {
    this.utils = utils;
  }

  public ASTNodeProcessor isProgram(Utils utils, LinkedList<Symbol> symbols,
      ASTNodeProcessor nodeProcessor)
      throws UnrecognisedTokenException, ParserException {
    setUtils(utils);
    setAllSymbols(symbols);
    this.addObserver(utils);
    savedPosition.addObserver(utils);
    savedPosition.add();
    int previousPosition = savedPosition.getSavedPosition();

    boolean matched = checkNode(isDeclaration(savedPosition), nodeProcessor) ||
        checkNode(isRecord(savedPosition), nodeProcessor) ||
        checkNode(isFunction(savedPosition), nodeProcessor) ||
        checkNode(isStatement(savedPosition), nodeProcessor);

    if (matched) {
      currentPosition = savedPosition.getSavedPosition();
    } else {
      Symbol incorrectSymbol = allSymbols.get(savedPosition.getSavedPosition());
      throw new ParserException(incorrectSymbol.getToken(),
          Integer.toString(incorrectSymbol.getLine_number()));
    }

    if (previousPosition == savedPosition.getSavedPosition()) {
      throw new ParserException("Parser is stuck at position ", "2");
    }
    return nodeProcessor;
  }

  public MainNode isDeclaration(Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    return new Declaration(utils, savedPosition).isConstant();
  }

  public MainNode isRecord(Position savedPosition)
      throws ParserException, UnrecognisedTokenException {
    return new Record(utils, savedPosition).isRecord();
  }

  public MainNode isFunction(Position savedPosition)
      throws ParserException, UnrecognisedTokenException {
    return new Function(utils, savedPosition).isFunction();
  }

  public MainNode isStatement(Position savedPosition)
      throws ParserException, UnrecognisedTokenException {
    return new StatementList(utils, savedPosition).statementList();
  }

  public boolean checkNode(MainNode astNode, ASTNodeProcessor nodeProcessor) {
    if (astNode != null) {
      astNode.accept(nodeProcessor);
      return true;
    }
    return false;
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
      observer.updatePosition(savedPosition);
    }
  }
}
