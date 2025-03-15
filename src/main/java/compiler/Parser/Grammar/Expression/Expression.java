package compiler.Parser.Grammar.Expression;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Assignment.Assignment;
import compiler.Parser.Grammar.Function.FunctionCall;
import compiler.Parser.Grammar.Statement.StatementList;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
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

    primary();
    expressionTail();

    return new MainNode(nodeName, expressionNode);
  }

  private void expressionTail() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(TokenType.OPERATOR, false)) {
      binary();
      primary();
      expressionTail();
      return;
    }
    return;
  }

  private void binary() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(TokenType.OPERATOR, true)) {
      expressionNode.addLast(utils.getGenericNode());
      return;
    }
    return;
  }

  private void primary() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(TokenType.LITERAL, true)) {
      expressionNode.addLast(utils.getGenericNode());
      return;
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
        MainNode mainNode = new Assignment(utils, savedPosition).assignment();
        expressionNode.addLast(mainNode);
      }

      return;
    } else if (utils.matchIndex(TokenType.LPAREN, true)) {
      expressionNode.addLast(utils.getGenericNode());
      MainNode expression = expression();
      if (utils.matchIndex(TokenType.RPAREN, true)) {
        expressionNode.addLast(utils.getGenericNode());
        return;
      }
    }
    return;
  }
}
