package compiler.Parser.Grammar.Statement;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Assignment.Assignment;
import compiler.Parser.Grammar.ControlStructure.ControlStructure;
import compiler.Parser.Grammar.Deallocation.Free;
import compiler.Parser.Grammar.Declaration.Declaration;
import compiler.Parser.Grammar.Function.FunctionCall;
import compiler.Parser.Grammar.Function.Return;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;

public class StatementList {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition;
  LinkedList<ASTNode> statementListNode;
  private final String nodeName = "StatementList";

  public StatementList(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    currentPosition = savedPosition.getSavedPosition();
    statementListNode = new LinkedList<>();
  }

  public MainNode statementList() throws ParserException, UnrecognisedTokenException {
    if (!isStatementStart()) {
      return null;
    }
    statement();
    statementList();

    return new MainNode(nodeName, statementListNode);
  }

  private ASTNode statement() throws ParserException, UnrecognisedTokenException {
    MainNode declarationNode = new Declaration(utils, savedPosition).isConstant();
    if (declarationNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      statementListNode.addLast(declarationNode);
      return declarationNode;
    }
    savedPosition.setSavedPosition(currentPosition);
    MainNode assignmentExpressionNode = new Assignment(utils,
        savedPosition).assignment();
    if (assignmentExpressionNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      statementListNode.addLast(assignmentExpressionNode);
      return assignmentExpressionNode;
    }
    savedPosition.setSavedPosition(currentPosition);

    MainNode callFunctionNode = new FunctionCall(utils, savedPosition).isFunctionCall();
    if (callFunctionNode != null) {
      statementListNode.addLast(callFunctionNode);
      /*todo*/
      if (utils.matchIndex(TokenType.SEMICOLON, true)) {
        currentPosition = savedPosition.getSavedPosition();
        statementListNode.addLast(utils.getGenericNode());
      }
      return callFunctionNode;
    }
    savedPosition.setSavedPosition(currentPosition);

    MainNode returnNode = new Return(utils, savedPosition).isReturn();
    if (returnNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      statementListNode.addLast(returnNode);
      return returnNode;
    }

    savedPosition.setSavedPosition(currentPosition);

    MainNode controlStructureNode = new ControlStructure(utils, savedPosition).controlStructure();
    if (controlStructureNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      statementListNode.addLast(controlStructureNode);

      return controlStructureNode;
    }

    savedPosition.setSavedPosition(currentPosition);

    MainNode freeNode = new Free(utils, savedPosition).free();
    if (freeNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      statementListNode.addLast(freeNode);

      return freeNode;
    }
    return null;
  }

  private boolean isStatementStart() throws UnrecognisedTokenException, ParserException {

    return utils.matchIndex(TokenType.KEYWORD, false) ||
        utils.matchIndex(TokenType.IDENTIFIER, false) ||
        utils.matchIndex(TokenType.RECORD, false) ||
        utils.matchIndex(TokenType.BUILTINFUNCTION, false);
  }

}
