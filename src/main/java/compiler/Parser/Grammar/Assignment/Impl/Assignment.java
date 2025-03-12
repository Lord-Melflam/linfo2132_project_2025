package compiler.Parser.Grammar.Assignment.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.FunctionCall;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;


public class Assignment {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition;
  LinkedList<ASTNode> assignmentNode;
  private final String nodeName = "Assignment";

  public Assignment(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
    assignmentNode = new LinkedList<>();
  }

  public MainNode assignment() throws ParserException, UnrecognisedTokenException {

    IdentifierNode identifierNode = null;
    if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
      assignmentNode.addLast(utils.getGenericNode());
    }

    if (utils.matchIndex(TokenType.DOT, true)) {
      assignmentNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
        assignmentNode.addLast(utils.getGenericNode());
        return recordAccess();
      }
    } else if (utils.matchIndex(TokenType.LBRACKET, true)) {
      assignmentNode.addLast(utils.getGenericNode());
      handleArrayOrFunctionCall();
    }

    if (utils.matchIndex(TokenType.ASSIGNMENT, true)) {
      assignmentNode.addLast(utils.getGenericNode());
      return handleAssignment();
    }

    return null;
  }

  private MainNode handleArrayOrFunctionCall() throws ParserException, UnrecognisedTokenException {

    currentPosition = savedPosition.getSavedPosition();
    MainNode callFunctionNode = new FunctionCall(utils, savedPosition).isFunctionCall();
    if (callFunctionNode == null) {
      savedPosition.setSavedPosition(currentPosition);
      MainNode expressionNode = new Expression(utils, savedPosition).expression();
      assignmentNode.addLast(expressionNode);
    } else {
      assignmentNode.addLast(callFunctionNode);
    }
    return arrayAccess();
  }

  private MainNode handleAssignment() throws ParserException, UnrecognisedTokenException {

    MainNode expression = new Expression(utils, savedPosition).expression();
    assignmentNode.addLast(expression);
    if (utils.matchIndex(TokenType.SEMICOLON, true)) {
      assignmentNode.addLast(utils.getGenericNode());
      return new MainNode(nodeName, assignmentNode);
    }
    return null;
  }

  private MainNode arrayAccess() throws ParserException, UnrecognisedTokenException {

    if (utils.matchIndex(TokenType.RBRACKET, true)) {
      assignmentNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.DOT, false) || utils.matchIndex(TokenType.LBRACKET, false)) {
        MainNode nestedAssignment = assignment();
        if (nestedAssignment != null) {
          assignmentNode.addLast(nestedAssignment);
          return nestedAssignment;
        }
      }

      return new MainNode(nodeName, assignmentNode);
    }

    return null;
  }

  private MainNode recordAccess() throws ParserException, UnrecognisedTokenException {

    if (utils.matchIndex(TokenType.DOT, false) || utils.matchIndex(TokenType.LBRACKET, false)) {
      MainNode nestedAssignment = assignment();
      if (nestedAssignment != null) {
        assignmentNode.addLast(nestedAssignment);
        return nestedAssignment;
      }
    }

    return new MainNode(nodeName, assignmentNode);
  }
}
