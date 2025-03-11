package compiler.Parser.Grammar.Statement.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Assignment.Impl.Assignment;
import compiler.Parser.Grammar.Assignment.Node.AssignmentExpressionNode;
import compiler.Parser.Grammar.ControlStructure.Impl.ControlStructure;
import compiler.Parser.Grammar.Deallocation.Impl.Free;
import compiler.Parser.Grammar.Deallocation.Node.FreeNode;
import compiler.Parser.Grammar.Declaration.Constant.Impl.Constant;
import compiler.Parser.Grammar.Declaration.Constant.Node.ConstantNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.FunctionCall;
import compiler.Parser.Grammar.Declaration.Function.Node.CallFunctionNode;
import compiler.Parser.Grammar.Statement.Node.ReturnNode;
import compiler.Parser.Grammar.Statement.Node.StatementListNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;

public class StatementList {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition;

  public StatementList(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    currentPosition = savedPosition.getSavedPosition();
  }

  public ASTNode statementList() throws ParserException, UnrecognisedTokenException {
    if (!isStatementStart()) {
      return null;
    }
    ASTNode statementNode = statement();
    ASTNode statementListNode = this.statementList();

    return new StatementListNode(statementNode, statementListNode);
  }

  private ASTNode statement() throws ParserException, UnrecognisedTokenException {
    ConstantNode constantNode = new Constant(utils, savedPosition).isConstant();
    if (constantNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      return constantNode;
    }
    savedPosition.setSavedPosition(currentPosition);
    AssignmentExpressionNode assignmentExpressionNode = new Assignment(utils,
        savedPosition).assignment();
    if (assignmentExpressionNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      return assignmentExpressionNode;
    }
    savedPosition.setSavedPosition(currentPosition);

    CallFunctionNode callFunctionNode = new FunctionCall(utils, savedPosition).isFunctionCall();
    if (callFunctionNode != null) {
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.SEMICOLON)) {
        savedPosition.add();
        currentPosition = savedPosition.getSavedPosition();
      }
      return callFunctionNode;
    }
    savedPosition.setSavedPosition(currentPosition);

    ReturnNode returnNode = new Return(utils, savedPosition).isReturn();
    if (returnNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      return returnNode;
    }

    savedPosition.setSavedPosition(currentPosition);

    ASTNode controlStructureNod = new ControlStructure(utils, savedPosition).controlStructure();
    if (controlStructureNod != null) {
      currentPosition = savedPosition.getSavedPosition();
      return controlStructureNod;
    }

    savedPosition.setSavedPosition(currentPosition);

    FreeNode freeNode = new Free(utils, savedPosition).free();
    if (freeNode != null) {
      currentPosition = savedPosition.getSavedPosition();
      return freeNode;
    }
    return null;
  }

  private boolean isStatementStart() throws UnrecognisedTokenException, ParserException {

    return utils.matchIndex(savedPosition.getSavedPosition(), TokenType.KEYWORD) ||
        utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER) ||
        utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RECORD) ||
        utils.matchIndex(savedPosition.getSavedPosition(), TokenType.BUILTINFUNCTION);
  }

}
 /* try {
      return new Control(utils).isFunctionCall();
    } catch (Exception e) {
      utils.restorePosition(savedPosition);
    }*/