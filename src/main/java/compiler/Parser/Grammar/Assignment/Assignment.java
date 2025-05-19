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
import java.util.List;


public class Assignment {

  private final Utils utils;
  private final Position savedPosition;
  private LinkedList<ASTNode> assignmentNode;
  private String nodeName = "Assignment";
  private int line;
  LinkedList<ASTNode> array = new LinkedList<>();
  LinkedList<ASTNode> recordAssignment = new LinkedList<>();
  LinkedList<ASTNode> recordAttribute = new LinkedList<>();

  public Assignment(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    assignmentNode = new LinkedList<>();
    line = utils.getLine();
  }

  public Assignment(Utils utils, Position savedPosition, ASTNode identifier) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    assignmentNode = new LinkedList<>();
    line = utils.getLine();
    assignmentNode.addLast(new MainNode("RecordName", List.of(identifier), line));
  }

  public MainNode assignment() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.DOT, true)) {
      //assignmentNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
        nodeName = "RecordAssignment";
        //assignmentNode.addLast(utils.getGenericNode());
        array.clear();
        recordAttribute.clear();
        array.addLast(utils.getGenericNode());
        recordAttribute.addLast(utils.getGenericNode());
        if (utils.matchIndex(TokenType.ASSIGNMENT, true)) {
          assignmentNode.addLast(new MainNode("RecordAttribute", recordAttribute, line));
          recordAttribute.clear();
          recordAttribute.addLast(utils.getGenericNode());
          MainNode expressionNode = new Expression(utils, savedPosition).expression();
          recordAttribute.addLast(expressionNode);
          //assignmentNode.addLast(expressionNode);
          if (utils.matchIndex(TokenType.SEMICOLON, true)) {
            //assignmentNode.addLast(utils.getGenericNode());
            recordAttribute.addLast(utils.getGenericNode());
            assignmentNode.addLast(new MainNode("RecordAssignment", recordAttribute, line));
            recordAttribute.clear();
            return new MainNode(nodeName, assignmentNode, line);
          }

        }
        MainNode node = recordAccess();
        return node;
      }
    } else if (utils.matchIndex(TokenType.LBRACKET, true)) {
      //assignmentNode.addLast(utils.getGenericNode());
      array.addLast(utils.getGenericNode());
      return handleArrayOrFunctionCall();
    }

    /*if (utils.matchIndex(TokenType.ASSIGNMENT, true)) {
      assignmentNode.addLast(utils.getGenericNode());
      return handleAssignment();
    }*/

    utils.throwParserException();
    return null;
  }

  private MainNode handleArrayOrFunctionCall() throws ParserException, UnrecognisedTokenException {
    MainNode expressionNode = new Expression(utils, savedPosition).expression();
    //assignmentNode.addLast(expressionNode);
    array.addLast(expressionNode);

    return arrayAccess();
  }

  private MainNode handleAssignment() throws ParserException, UnrecognisedTokenException {
    MainNode expressionNode = new Expression(utils, savedPosition).expression();
    recordAssignment.addLast(expressionNode);
    if (utils.matchIndex(TokenType.SEMICOLON, true)) {
      recordAssignment.addLast(utils.getGenericNode());
      assignmentNode.addLast(new MainNode("RecordAssignment", recordAssignment, line));
      recordAssignment.clear();
      return new MainNode(nodeName, assignmentNode, line);
    }
    utils.throwParserException();
    return null;
  }

  private MainNode arrayAccess() throws ParserException, UnrecognisedTokenException {

    if (utils.matchIndex(TokenType.RBRACKET, true)) {
      //assignmentNode.addLast(utils.getGenericNode());
      array.addLast(utils.getGenericNode());
      assignmentNode.addLast(new MainNode("RecordArrayAttribute", array, utils.getLine()));
      array.clear();
      return isArrayOperation();
    }

    utils.throwParserException();
    return null;
  }

  private MainNode isArrayOperation() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.DOT, false) || utils.matchIndex(TokenType.LBRACKET, false)) {
      if (utils.getSymbol(savedPosition.getSavedPosition()).getToken()
          .equals(TokenType.DOT.getValue())) {
        System.out.println();
        if (array.size() == 1) {
          assignmentNode.addLast(new MainNode("RecordAttribute", array, utils.getLine()));
          array.clear();
        }
      }
      MainNode nestedAssignment = assignment();
      if (nestedAssignment != null) {
        assignmentNode.addLast(nestedAssignment);
        return nestedAssignment;
      }
    } else if (utils.matchIndex(TokenType.ASSIGNMENT, true)) {
      recordAssignment.addLast(utils.getGenericNode());
      return handleAssignment();
    }

    return new MainNode(nodeName, assignmentNode, line);
  }

  private MainNode recordAccess() throws ParserException, UnrecognisedTokenException {
    return isArrayOperation();
  }
}
