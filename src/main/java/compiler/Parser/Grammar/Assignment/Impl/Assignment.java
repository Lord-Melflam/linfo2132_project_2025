package compiler.Parser.Grammar.Assignment.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.AssignmentNode;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Assignment.Node.AssignmentExpressionNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.FunctionCall;
import compiler.Parser.Grammar.Declaration.Function.Node.CallFunctionNode;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;

public class Assignment {

  private final Utils utils;
  private Position savedPosition;
  private int currentPosition;

  public Assignment(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
  }

  public AssignmentExpressionNode assignment() throws ParserException, UnrecognisedTokenException {

    AssignmentExpressionNode arrayAccessNode = null;

    AssignmentExpressionNode recordAccessNode = null;
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER)
        || utils.matchIndex(savedPosition.getSavedPosition(), TokenType.DOT) || utils.matchIndex(
        savedPosition.getSavedPosition(), TokenType.LBRACKET)) {
/*
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Identifier.name());
*/
      IdentifierNode identifierNode2 = null;
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER)) {
        savedPosition.add();
        identifierNode2 = new IdentifierNode("jhjhhj");
      }

      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.DOT)) {
/*
        utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.SpecialSymbol.name());
*/
        savedPosition.add();
        SpecialSymbolNode specialSymbolNode = new SpecialSymbolNode(null);
        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER)) {
/*
          utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Identifier.name());
*/
          savedPosition.add();
          IdentifierNode identifierNode = new IdentifierNode(null);
          recordAccessNode = recordAccess(identifierNode2, specialSymbolNode, identifierNode);
        }
      } else if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LBRACKET)) {
        savedPosition.add();

        currentPosition = savedPosition.getSavedPosition();
        CallFunctionNode callFunctionNode = new FunctionCall(utils, savedPosition).isFunctionCall();
        if (callFunctionNode == null) {
          savedPosition.setSavedPosition(currentPosition);
          ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
        }
        arrayAccessNode = arrayAccess(identifierNode2, null, null);
      }

      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.ASSIGNMENT)) {
/*
        return null;
*/

        savedPosition.add();
        AssignmentNode assignmentNode = new AssignmentNode();
        ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();

        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.SEMICOLON)) {
/*
        return null;
*/

          savedPosition.add();
          SpecialSymbolNode specialSymbolNode = new SpecialSymbolNode(null);

          AssignmentExpressionNode assignmentExpressionNode = AssignmentExpressionNode.builder()
              .assignmentNode(assignmentNode).expressionNode(null)
              .specialSymbolNode(specialSymbolNode).build();

          if (assignmentExpressionNode.getRecordAccessNode() != null) {
            assignmentExpressionNode.setRecordAccessNode(recordAccessNode);
          } else if (assignmentExpressionNode.getArrayAccessNode() != null) {
            assignmentExpressionNode.setArrayAccessNode(arrayAccessNode);
          } else {
            assignmentExpressionNode.setIdentifierNode(identifierNode2);
          }

          return assignmentExpressionNode;
        }
      }
    }
    return null;
  }

  private AssignmentExpressionNode arrayAccess(IdentifierNode identifierNode2,
      SpecialSymbolNode specialSymbolNodeOpen, ExpressionNode expressionNode)
      throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RBRACKET)) {
      savedPosition.add();
      SpecialSymbolNode specialSymbolNodeClose = new SpecialSymbolNode(null);
      AssignmentExpressionNode assignmentExpressionNode = null;
      /*if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.DOT) || utils.matchIndex(
          savedPosition.getSavedPosition(), TokenType.LBRACKET)) {
        assignmentExpressionNode = assignment();

      }*/
     /* return new ArrayAccessNode(assignmentExpressionNode, identifierNode2, specialSymbolNodeOpen,
          expressionNode, specialSymbolNodeClose);*/
      return assignment();
    }
    return null;

  }

  private AssignmentExpressionNode recordAccess(IdentifierNode identifierNode2,
      SpecialSymbolNode specialSymbolNode, IdentifierNode identifierNode)
      throws ParserException, UnrecognisedTokenException {
    AssignmentExpressionNode assignmentExpressionNode = null;
   /* if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.DOT) || utils.matchIndex(
        savedPosition.getSavedPosition(), TokenType.LBRACKET)) {
      assignmentExpressionNode = assignment();

    }
    return new RecordAccessNode(assignmentExpressionNode, identifierNode2, specialSymbolNode,
        identifierNode);*/
    return assignment();
  }
}
