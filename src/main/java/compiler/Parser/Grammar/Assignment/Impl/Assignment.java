package compiler.Parser.Grammar.Assignment.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Utils.Utils;

public class Assignment {

  private final Utils utils;

  public Assignment(Utils utils) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
  }


}
