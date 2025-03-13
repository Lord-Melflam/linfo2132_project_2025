package compiler.Parser;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Parser.Grammar.Program.Program;
import compiler.Parser.Utils.ASTNodeProcessor;
import compiler.Parser.Utils.Utils;

public class Parser {


  private final Program program;

  public Parser(Lexer lexer) throws UnrecognisedTokenException, ParserException {
    Utils utils = new Utils(lexer.getSymbols());
    program = new Program(utils, lexer);
    getAST();
  }

  public void getAST() throws UnrecognisedTokenException, ParserException {
    ASTNodeProcessor astNodeProcessor = program.isProgram();
    astNodeProcessor.printTree("");
  }
}
