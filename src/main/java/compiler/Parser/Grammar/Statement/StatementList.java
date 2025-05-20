package compiler.Parser.Grammar.Statement;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Symbol;
import compiler.Parser.ASTNode.GenericNode;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Assignment.Assignment;
import compiler.Parser.Grammar.ControlStructure.Break;
import compiler.Parser.Grammar.ControlStructure.Continue;
import compiler.Parser.Grammar.ControlStructure.ControlStructure;
import compiler.Parser.Grammar.Deallocation.Free;
import compiler.Parser.Grammar.Declaration.Declaration;
import compiler.Parser.Grammar.Expression.Expression;
import compiler.Parser.Grammar.Function.Return;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;

public class StatementList {

  private final Utils utils;
  private final Position savedPosition;
  LinkedList<ASTNode> statementListNode;
  private final String nodeName = "Statements";
  private int line;

  public StatementList(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    statementListNode = new LinkedList<>();
    line = utils.getLine();
  }

  public MainNode statementList() throws ParserException, UnrecognisedTokenException {
    if (!isStatementStart()) {
      return null;
    }
    statement();
    statementList();

    return new MainNode(nodeName, statementListNode, line);
  }

  @SuppressWarnings("unchecked")
  private void statement() throws ParserException, UnrecognisedTokenException {
    Symbol symbol = utils.getSymbol(savedPosition.getSavedPosition());
    switch (symbol.getName()) {
      case "Keyword" -> {
        if (symbol.getToken().equals(TokenType.RETURN.getValue())) {
          MainNode returnNode = new Return(utils, savedPosition).isReturn();
          if (returnNode != null) {
            statementListNode.addLast(returnNode);
            return;
          }
          return;
        }
        if (symbol.getToken().equals(TokenType.FOR.getValue()) || symbol.getToken()
            .equals(TokenType.WHILE.getValue()) || symbol.getToken()
            .equals(TokenType.IF.getValue())) {
          MainNode controlStructureNode = new ControlStructure(utils,
              savedPosition).controlStructure();
          if (controlStructureNode != null) {
            statementListNode.addLast(controlStructureNode);
            return;
          }
        }
        if (symbol.getToken().equals(TokenType.FREE.getValue())) {
          MainNode freeNode = new Free(utils, savedPosition).free();
          if (freeNode != null) {
            statementListNode.addLast(freeNode);

            return;
          }
        }
        if (symbol.getToken().equals("break")) {
          MainNode freeNode = new Break(utils, savedPosition).free();
          if (freeNode != null) {
            statementListNode.addLast(freeNode);

            return;
          }
        }
        if (symbol.getToken().equals("continue")) {
          MainNode freeNode = new Continue(utils, savedPosition).free();
          if (freeNode != null) {
            statementListNode.addLast(freeNode);
            return;
          }
        }
        return;
      }
      case "Assignment" -> {
        MainNode assignmentExpressionNode = new Assignment(utils, savedPosition).assignment();
        if (assignmentExpressionNode != null) {
          statementListNode.addLast(assignmentExpressionNode);
          return;
        }
        return;
      }
      case "Punctuation" -> {
        if (symbol.getToken().equals(TokenType.DOT.getValue())) {
          MainNode assignmentExpressionNode = new Assignment(utils, savedPosition).assignment();
          if (assignmentExpressionNode != null) {
            statementListNode.addLast(assignmentExpressionNode);
            return;
          }
          return;
        }
        return;

      }
      case "Operator" -> {
        if (symbol.getToken().equals(TokenType.MINUS.getValue())) {
          parseExpression();
        }
        return;
      }
      case "Literal", "Record", "BuiltInFunction" -> {
        parseExpression();
        return;
      }
      case "SpecialSymbol" -> {
        if (symbol.getToken().equals(TokenType.LPAREN.getValue())) {
          parseExpression();
        }
        return;
      }
      case "Identifier" -> {
        GenericNode<String> getGenericNode = null;
        if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
          getGenericNode = utils.getGenericNode();
          //statementListNode.addLast(getGenericNode);
          Symbol next = utils.getSymbol(savedPosition.getSavedPosition());
          if (next.getName().equals(TokenType.TYPESPECIFIER.getCategory()) || next.getName()
              .equals(TokenType.RECORD.getCategory())) {
            MainNode declarationNode = new Declaration(utils, savedPosition,
                getGenericNode).declaration();
            if (declarationNode != null) {
              statementListNode.addLast(declarationNode);
              return;
            }
            return;
          }
          if (next.getToken().equals(TokenType.DOT.getValue()) || next.getToken()
              .equals(TokenType.LBRACKET.getValue())) {

            MainNode assignmentExpressionNode = new Assignment(utils, savedPosition,
                getGenericNode).assignment();
            if (assignmentExpressionNode != null) {
              statementListNode.addLast(assignmentExpressionNode);
              return;
            }
            return;
          }

          if (next.getToken().equals(TokenType.ASSIGNMENT.getValue())) {
            MainNode declarationNode = new Declaration(utils, savedPosition,
                getGenericNode).initialisation();
            /*todo*/
            /*MainNode assignmentExpressionNode = new Assignment(utils, savedPosition,
                getGenericNode).assignment();*/
            if (declarationNode != null) {
              statementListNode.addLast(declarationNode);
              return;
            }
            return;
          }


        }
        parseExpression(getGenericNode);
      }
      default -> {
        utils.throwParserException();
        return;
      }
    }
  }

  private void parseExpression(GenericNode<String> getGenericNode)
      throws UnrecognisedTokenException, ParserException {
    savedPosition.setSavedPosition(savedPosition.getSavedPosition() - 1);
    MainNode callFunctionNode = new Expression(utils, savedPosition, getGenericNode).expression();
    if (callFunctionNode != null) {
      statementListNode.addLast(callFunctionNode);
      if (utils.matchIndex(TokenType.SEMICOLON, true)) {
        statementListNode.addLast(utils.getGenericNode());
      }
      return;
    }
    utils.throwParserException();
    return;
  }

  private void parseExpression() throws UnrecognisedTokenException, ParserException {
    MainNode callFunctionNode = new Expression(utils, savedPosition).expression();
    if (callFunctionNode != null) {
      statementListNode.addLast(callFunctionNode);
      if (utils.matchIndex(TokenType.SEMICOLON, true)) {
        statementListNode.addLast(utils.getGenericNode());
      }
      return;
    }
    utils.throwParserException();
    return;
  }

  private boolean isStatementStart() throws UnrecognisedTokenException, ParserException {

    return utils.matchIndex(TokenType.KEYWORD, false) || utils.matchIndex(TokenType.IDENTIFIER,
        false) || utils.matchIndex(TokenType.RECORD, false) || utils.matchIndex(
        TokenType.BUILTINFUNCTION, false);
  }

}
