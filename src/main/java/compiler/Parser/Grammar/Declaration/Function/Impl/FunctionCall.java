package compiler.Parser.Grammar.Declaration.Function.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Declaration.Function.Node.CallFunctionNode;
import compiler.Parser.Grammar.Declaration.Function.Node.FunctionCallNode;
import compiler.Parser.Grammar.Declaration.Function.Node.ParameterListNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;

public class FunctionCall {

  private final Utils utils;
  private final Position savedPosition;

  public FunctionCall(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
  }

  public CallFunctionNode isFunctionCall() throws ParserException, UnrecognisedTokenException {
    FunctionCallNode functionCallNode = functionCall();
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RPAREN)
        && functionCallNode != null) {
      savedPosition.add();
      SpecialSymbolNode specialSymbolNode1 = new SpecialSymbolNode(null);
      return new CallFunctionNode(functionCallNode, specialSymbolNode1);
    }
    return null;
  }

  private FunctionCallNode functionCall() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER)
        || utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RECORD)
        || utils.matchIndex(savedPosition.getSavedPosition(), TokenType.BUILTINFUNCTION)
        || utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LPAREN)) {
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER)) {
        savedPosition.add();
        IdentifierNode identifierNode = new IdentifierNode(null);
      }
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RECORD)) {
        savedPosition.add();
      }
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.BUILTINFUNCTION)) {
        savedPosition.add();
      }
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LPAREN)) {
        savedPosition.add();
        SpecialSymbolNode specialSymbolNode1 = new SpecialSymbolNode("hhghgghhg");
        ParameterListNode parameterListNode = new ParameterList(utils,
            savedPosition).isParameterList();
        SpecialSymbolNode specialSymbolNode2 = new SpecialSymbolNode("ggh");
        return new FunctionCallNode(/*identifierNode,*/ specialSymbolNode1, parameterListNode,
            specialSymbolNode2);
      }
    }
    return null;
  }
}
