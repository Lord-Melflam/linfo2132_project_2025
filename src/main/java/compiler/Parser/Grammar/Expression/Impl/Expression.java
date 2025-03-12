package compiler.Parser.Grammar.Expression.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Assignment.Impl.Assignment;
import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.FunctionCall;
import compiler.Parser.Grammar.Expression.Node.BinaryOperator;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Grammar.Expression.Node.PrimaryOperator;
import compiler.Parser.Grammar.Statement.Impl.StatementList;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;

public class Expression {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition = 0;
  LinkedList<ASTNode> expressionNode;
  private final String nodeName = "Expression";

  public Expression(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
    expressionNode = new LinkedList<>();

  }

  public MainNode expression() throws UnrecognisedTokenException, ParserException {

    ASTNode primary = primary();
    ASTNode expressionTail = expressionTail();

    return new MainNode(nodeName, expressionNode);
  }

  private ASTNode expressionTail() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(TokenType.OPERATOR, false)) {
      return new ExpressionNode(binary(), primary(), expressionTail());
    }
    return null;
  }

  private ASTNode binary() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(TokenType.OPERATOR, true)) {
      expressionNode.addLast(utils.getGenericNode());
      return new BinaryOperator(null);
    }
    return null;
  }

  private ASTNode primary() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(TokenType.LITERAL, true)) {
      expressionNode.addLast(utils.getGenericNode());
      return new PrimaryOperator(null, null, null);
    } else if (utils.matchIndex(TokenType.IDENTIFIER, true)
        || utils.matchIndex(TokenType.BUILTINFUNCTION, true)) {
      expressionNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.LPAREN, false)) {
        expressionNode.addLast(utils.getGenericNode());
        currentPosition = savedPosition.getSavedPosition();
        MainNode callFunctionNode = new FunctionCall(utils, savedPosition).isFunctionCall();
        if (callFunctionNode == null) {
          savedPosition.setSavedPosition(currentPosition);
          ASTNode astNode = new StatementList(utils, savedPosition).statementList();
          if (astNode == null) {
            savedPosition.setSavedPosition(currentPosition);
            MainNode node = new Expression(utils, savedPosition).expression();
            expressionNode.addLast(node);
          } else {
            expressionNode.addLast(astNode);
          }
        } else {
          expressionNode.addLast(callFunctionNode);
        }
      } else if (utils.matchIndex(TokenType.DOT, false) || utils.matchIndex(TokenType.LBRACKET,
          false)) {
        expressionNode.addLast(utils.getGenericNode());
        MainNode mainNode = new Assignment(utils, savedPosition).assignment();
        expressionNode.addLast(mainNode);
      }

      return new PrimaryOperator(null, null, null);
    } else if (utils.matchIndex(TokenType.LPAREN, true)) {
      expressionNode.addLast(utils.getGenericNode());
      MainNode expression = expression();
      expressionNode.addLast(expression);
      if (utils.matchIndex(TokenType.RPAREN, true)) {
        expressionNode.addLast(utils.getGenericNode());
        return new PrimaryOperator(null, null, null);
      }
    }
    return null;
  }
}
