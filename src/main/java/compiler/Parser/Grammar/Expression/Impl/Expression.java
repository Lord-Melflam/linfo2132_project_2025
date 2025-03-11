package compiler.Parser.Grammar.Expression.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.FunctionCall;
import compiler.Parser.Grammar.Declaration.Function.Node.CallFunctionNode;
import compiler.Parser.Grammar.Expression.BinaryOperator;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Grammar.Expression.PrimaryOperator;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;

public class Expression {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition = 0;

  public Expression(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
  }

  public ExpressionNode expression() throws UnrecognisedTokenException, ParserException {

    ASTNode primary = primary();
    ASTNode expressionTail = expressionTail();

    return new ExpressionNode(primary, expressionTail);
  }

  private ASTNode expressionTail() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.OPERATOR)) {
      return new ExpressionNode(binary(), primary(), expressionTail());
    }
    return null;
  }

  private ASTNode binary() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.OPERATOR)) {
      savedPosition.add();
      return new BinaryOperator(null);
    }
    return null;
  }

  private ASTNode primary() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LITERAL)) {
      savedPosition.add();
      return new PrimaryOperator(null, null, null);
    } else if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER)
        || utils.matchIndex(savedPosition.getSavedPosition(), TokenType.BUILTINFUNCTION)) {
      savedPosition.add();
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LPAREN)) {
        currentPosition = savedPosition.getSavedPosition();
        CallFunctionNode callFunctionNode = new FunctionCall(utils, savedPosition).isFunctionCall();
        if (callFunctionNode == null) {
          savedPosition.setSavedPosition(currentPosition);
          ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
        }
      }

      return new PrimaryOperator(null, null, null);
    } else if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LPAREN)) {
      savedPosition.add();
      ASTNode specialSymbolOpen = new SpecialSymbolNode(null);
      ASTNode expression = expression();
      ASTNode specialSymbolClose = new SpecialSymbolNode(null);
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RPAREN)) {
        savedPosition.add();
        return new PrimaryOperator(null, null, null);
      }
    }
    return null;
  }
}
