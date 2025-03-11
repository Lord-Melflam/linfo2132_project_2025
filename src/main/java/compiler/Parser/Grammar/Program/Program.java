package compiler.Parser.Grammar.Program;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Constant.Impl.Constant;
import compiler.Parser.Grammar.Declaration.Constant.Node.ConstantNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.Function;
import compiler.Parser.Grammar.Declaration.Function.Node.FunctionNode;
import compiler.Parser.Grammar.Declaration.Record.Impl.Record;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordNode;
import compiler.Parser.Grammar.Statement.Impl.StatementList;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;

public class Program {

  private final Utils utils;

  public Program(Utils utils) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
  }

  public ConstantNode isProgram(Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    return new Constant(utils, savedPosition).isConstant();
  }

  public RecordNode isRecord(Position savedPosition)
      throws ParserException, UnrecognisedTokenException {
    return new Record(utils, savedPosition).isRecord();
  }

  public FunctionNode isFunction(Position savedPosition)
      throws ParserException, UnrecognisedTokenException {
    return new Function(utils, savedPosition).isFunction();
  }

  public ASTNode isStatement(Position savedPosition)
      throws ParserException, UnrecognisedTokenException {
    return new StatementList(utils, savedPosition).statementList();
  }


}
