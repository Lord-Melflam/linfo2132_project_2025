package compiler.Parser.Grammar.Program;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.AST.RootNode;
import compiler.Parser.Grammar.Declaration.Constant.Impl.Constant;
import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.Function;
import compiler.Parser.Grammar.Declaration.Record.Impl.Record;
import compiler.Parser.Grammar.Statement.Impl.StatementList;
import compiler.Parser.Utils.ASTNodeProcessor;
import compiler.Parser.Utils.Interface.Observer;
import compiler.Parser.Utils.Interface.Subject;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Program implements Subject {

  private int currentPosition;

  private final Utils utils;
  private final ASTNodeProcessor nodeProcessor;
  private final Lexer lexer;
  private final List<Observer> observers = new ArrayList<>();
  private Symbol lookahead;
  private final LinkedList<Symbol> allSymbols;

  Position savedPosition;

  public Program(Utils utils, Lexer lexer) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    nodeProcessor = new ASTNodeProcessor();
    currentPosition = 0;
    this.lexer = lexer;
    savedPosition = new Position();
    allSymbols = lexer.getAllSymbolsClone();
    allSymbols.pop();
    this.addObserver(utils);
    savedPosition.addObserver(utils);
    savedPosition.add();
    this.lookahead = lexer.getNextSymbol();
  }

  public ASTNodeProcessor isProgram()
      throws UnrecognisedTokenException, ParserException {
    while (!allSymbols.get(savedPosition.getSavedPosition()).getName().equals("EndFile")) {
      int previousPosition = savedPosition.getSavedPosition();

      boolean matched = checkNode(isDeclaration(savedPosition)) ||
          checkNode(isRecord(savedPosition)) ||
          checkNode(isFunction(savedPosition)) ||
          checkNode(isStatement(savedPosition));

      if (matched) {
        currentPosition = savedPosition.getSavedPosition();
      } else {
        Symbol incorrectSymbol = getLookahead();
        throw new ParserException(
            incorrectSymbol.getToken(),
            "Unexpected token at line " + incorrectSymbol.getLine_number() +
                ": '" + incorrectSymbol.getName() + "'"
        );
      }

      if (previousPosition == savedPosition.getSavedPosition()) {
        throw new ParserException("Parser is stuck at position ", "2");
      }
    }
    return nodeProcessor;
  }

  public MainNode isDeclaration(Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    return new Constant(utils, savedPosition).isConstant();
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

  public boolean checkNode(MainNode astNode)
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

  public Symbol getLookahead() {
    return lookahead;
  }


  public Symbol nextSymbol() throws UnrecognisedTokenException {
    this.lookahead = lexer.getNextSymbol();
    notifyObservers();
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
      observer.updatePosition(savedPosition);
      observer.updateSymbol(lookahead);
    }
  }
}
