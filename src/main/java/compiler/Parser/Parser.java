package compiler.Parser;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Lexer;
import compiler.Parser.ASTNode.ASTNodeProcessor;
import compiler.Parser.Grammar.Program.Program;
import compiler.Parser.Utils.Utils;
import compiler.Parser.Utils.ASTUtils.ASTUtils;

import java.io.IOException;

public class Parser {


  private final Program program;

  public Parser(Lexer lexer) throws UnrecognisedTokenException, ParserException, IOException {
    Utils utils = new Utils(lexer.getSymbols());
    program = new Program(utils, lexer);
    getAST();
  }

  public ASTNodeProcessor getAST() throws UnrecognisedTokenException, ParserException, IOException {
    ASTNodeProcessor astNodeProcessor = program.isProgram();

    astNodeProcessor.printTree("");
    
    ASTUtils.saveAST(astNodeProcessor, "test/TestFile/AnswersParser/test.json");
    return astNodeProcessor;
  }
}
