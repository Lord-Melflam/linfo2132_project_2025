package compiler.Parser.Grammar.Program;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Constant.Impl.Constant;
import compiler.Parser.Grammar.Declaration.Constant.Node.ConstantNode;
import compiler.Parser.Grammar.Declaration.Global.Impl.Global;
import compiler.Parser.Grammar.Declaration.Global.Node.GlobalNode;
import compiler.Parser.Grammar.Declaration.Record.Impl.Record;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordNode;
import compiler.Parser.Utils.Utils;

public class Program {

  private final Constant constant;
  private final Record record;
  private final Global global;

  public Program(Utils utils) throws UnrecognisedTokenException, ParserException {
    constant = new Constant(utils);
    record = new Record(utils);
    global = new Global(utils);
  }

  public ConstantNode isProgram() throws UnrecognisedTokenException, ParserException {
    return constant.isConstant();
  }

  public RecordNode isRecord() throws ParserException, UnrecognisedTokenException {
    return record.isRecord();
  }

  public GlobalNode isGlobal() throws ParserException, UnrecognisedTokenException {
    return global.isGlobal();
  }

}
