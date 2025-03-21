package compiler.Parser.Grammar.Assignment;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Expression.Expression;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;


public class Assignment {

  private final Utils utils;
  private final Position savedPosition;
  private LinkedList<ASTNode> assignmentNode;
  private final String nodeName = "Assignment";

  public Assignment(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    assignmentNode = new LinkedList<>();
  }

  public Assignment(Utils utils, Position savedPosition, ASTNode identifier) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    assignmentNode = new LinkedList<>();
    assignmentNode.addLast(identifier);
  }

  public MainNode assignment() throws ParserException, UnrecognisedTokenException {
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

  private void handleArrayOrFunctionCall() throws ParserException, UnrecognisedTokenException {
    MainNode expressionNode = new Expression(utils, savedPosition).expression();
    assignmentNode.addLast(expressionNode);
    arrayAccess();
  }

  private MainNode handleAssignment() throws ParserException, UnrecognisedTokenException {
    MainNode expressionNode = new Expression(utils, savedPosition).expression();
    assignmentNode.addLast(expressionNode);
    if (utils.matchIndex(TokenType.SEMICOLON, true)) {
      assignmentNode.addLast(utils.getGenericNode());
      return new MainNode(nodeName, assignmentNode);
    }
    return null;
  }

  private MainNode arrayAccess() throws ParserException, UnrecognisedTokenException {

    if (utils.matchIndex(TokenType.RBRACKET, true)) {
      assignmentNode.addLast(utils.getGenericNode());
      return isArrayOperation();
    }

    return null;
  }

  private MainNode isArrayOperation() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.DOT, false) || utils.matchIndex(TokenType.LBRACKET, false)) {
      MainNode nestedAssignment = assignment();
      if (nestedAssignment != null) {
        assignmentNode.addLast(nestedAssignment);
        return nestedAssignment;
      }
    }

    return new MainNode(nodeName, assignmentNode);
  }

  private MainNode recordAccess() throws ParserException, UnrecognisedTokenException {
    return isArrayOperation();
  }
}
