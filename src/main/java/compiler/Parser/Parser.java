package compiler.Parser;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.AST.RootNode;
import compiler.Parser.Grammar.Declaration.Constant.Node.ConstantNode;
import compiler.Parser.Grammar.Declaration.Global.Node.GlobalNode;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordNode;
import compiler.Parser.Grammar.Program.Program;
import compiler.Parser.Utils.ASTNodeProcessor;
import compiler.Parser.Utils.Interface.Observer;
import compiler.Parser.Utils.Interface.Subject;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class Parser implements Subject {

  private List<Observer> observers = new ArrayList<>();
  private Lexer lexer;
  private Symbol lookahead;
  private Utils utils;
  private Program program;
  private ASTNodeProcessor nodeProcessor;

  public Parser(Lexer lexer) throws UnrecognisedTokenException, ParserException {
    utils = new Utils(this);
    this.lexer = lexer;
    this.addObserver(utils);
    this.lookahead = nextSymbol();
    program = new Program(utils);
    nodeProcessor = new ASTNodeProcessor();
    getAST();
  }

  public void getAST() throws UnrecognisedTokenException, ParserException {
    if (lexer.hasNext()) {
      getASTImpl();
    }
  }

  private void getASTImpl() throws ParserException, UnrecognisedTokenException {

    ConstantNode constantNode = program.isProgram();
    if (constantNode != null) {
      constantNode.toString();
      constantNode.accept(nodeProcessor);
      getAST();
      return;
    }
    getRootNode().printNodes();

    RecordNode recordNode = program.isRecord();
    if (recordNode != null) {
      recordNode.toString();
      recordNode.accept(nodeProcessor);
      getAST();
      return;
    }
    getRootNode().printNodes();

    GlobalNode globalNode = program.isGlobal();
    if (globalNode != null) {
      globalNode.toString();
      globalNode.accept(nodeProcessor);
      getRootNode().printNodes();
      getAST();
      return;
    }
    getRootNode().printNodes();
    return;
  /*  Symbol incorrectSymbol = getLookahead();
    throw new ParserException(incorrectSymbol.getToken(),
        Integer.toString(incorrectSymbol.getLine_number()));*/
  }

  public RootNode getRootNode() {
    return nodeProcessor.getRoot();
  }
 /* private void getASTImpl() throws ParserException, UnrecognisedTokenException {
    ConstantNode constantNode = program.isProgram();

    RecordNode recordNode = program.isRecord();

    if (constantNode != null) {
      System.out.println(constantNode.toString());
      getAST();
    } else {
      Symbol incorrectSymbol = getLookahead();
      return;
      throw new ParserException(incorrectSymbol.getToken(),
          Integer.toString(incorrectSymbol.getLine_number()));
    }
  }*/

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
      observer.update(lookahead);
    }
  }

}
