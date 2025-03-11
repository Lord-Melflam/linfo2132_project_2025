package compiler.Parser.Grammar.Declaration.Function.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Symbols.TypeSpecifier;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.TypeSpecifierNode;
import compiler.Parser.Grammar.Declaration.Function.Node.CallFunctionNode;
import compiler.Parser.Grammar.Declaration.Function.Node.ParameterListNode;
import compiler.Parser.Grammar.Declaration.Function.Node.ParameterListTailNode;
import compiler.Parser.Grammar.Declaration.Function.Node.ParameterNode;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;

public class ParameterList {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition;

  public ParameterList(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
  }

  public ParameterListNode isParameterList() throws ParserException, UnrecognisedTokenException {
    return new ParameterListNode(parameter(), parameterListTail());
  }

  private ParameterListTailNode parameterListTail()
      throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.COMMA)) {
      savedPosition.add();
      return new ParameterListTailNode(parameter(), parameterListTail());
    }
    return null;
  }

  private ParameterNode parameter() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.TYPESPECIFIER)) {
      savedPosition.add();
      TypeSpecifier typeSpecifier = new TypeSpecifier();
      TypeSpecifierNode typeSpecifierNode = new TypeSpecifierNode(null);
      savedPosition.add();
      IdentifierNode identifierNode = new IdentifierNode(null);

      return new ParameterNode(identifierNode, typeSpecifierNode);
    } else if (utils.matchIndex(savedPosition.getSavedPosition(),
        TokenType.RPAREN)) {
      return new ParameterNode(null, null);
    } else {
      currentPosition = savedPosition.getSavedPosition();
      CallFunctionNode callFunctionNode = new FunctionCall(utils, savedPosition).isFunctionCall();
      if (callFunctionNode == null) {
        savedPosition.setSavedPosition(currentPosition);
        ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
      }
      return new ParameterNode(null, null);

    }
  }
}
