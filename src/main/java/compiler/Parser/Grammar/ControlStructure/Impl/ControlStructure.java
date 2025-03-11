package compiler.Parser.Grammar.ControlStructure.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.KeywordNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.ControlStructure.Node.ForNode;
import compiler.Parser.Grammar.ControlStructure.Node.IfNode;
import compiler.Parser.Grammar.ControlStructure.Node.WhileNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.FunctionCall;
import compiler.Parser.Grammar.Declaration.Function.Node.CallFunctionNode;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Grammar.Statement.Impl.StatementList;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ControlStructure {

  private Utils utils;
  private int currentPosition;
  private Position savedPosition;
  private final List<HashSet<TokenType>> expectedSymbolsControlStructure = List.of(
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.TYPESPECIFIER, TokenType.RECORD)));

  public ControlStructure(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
  }

  public ASTNode controlStructure() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.FOR)) {
      savedPosition.add();
      KeywordNode keywordNode = new KeywordNode(null);
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LPAREN)) {
        savedPosition.add();
        SpecialSymbolNode specialSymbolNode = new SpecialSymbolNode(null);
        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER)) {
          savedPosition.add();
          IdentifierNode identifierNode = new IdentifierNode(null);
          return forLoop(keywordNode, specialSymbolNode, identifierNode);
        } else if (utils.lookahead_matches(savedPosition.getSavedPosition(),
            expectedSymbolsControlStructure)) {
          savedPosition.add(expectedSymbolsControlStructure);
          IdentifierNode identifierNode = new IdentifierNode(null);
          return forLoop(keywordNode, specialSymbolNode, identifierNode);
        }
      }
    }

    WhileNode whileNode = whileLoop();
    if (whileNode != null) {
      return whileNode;
    }

    return ifStatement();
  }

  /*for (i, 1, a, 1) {*/
  private ForNode forLoop(KeywordNode keywordNode, SpecialSymbolNode specialSymbolNode,
      IdentifierNode identifierNode) throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.COMMA)) {
      savedPosition.add();
      currentPosition = savedPosition.getSavedPosition();
      CallFunctionNode callFunctionNode = new FunctionCall(utils,
          savedPosition).isFunctionCall();
      if (callFunctionNode == null) {
        savedPosition.setSavedPosition(currentPosition);
        ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
      }

      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.COMMA)) {
        savedPosition.add();
        currentPosition = savedPosition.getSavedPosition();
        CallFunctionNode callFunctionNode2 = new FunctionCall(utils,
            savedPosition).isFunctionCall();
        if (callFunctionNode == null) {
          savedPosition.setSavedPosition(currentPosition);
          ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
        }
      }
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.COMMA)) {
        savedPosition.add();
        currentPosition = savedPosition.getSavedPosition();
        CallFunctionNode callFunctionNode2 = new FunctionCall(utils,
            savedPosition).isFunctionCall();
        if (callFunctionNode == null) {
          savedPosition.setSavedPosition(currentPosition);
          ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
        }
      }
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RPAREN)) {
        savedPosition.add();
        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LBRACE)) {
          savedPosition.add();
          new StatementList(utils, savedPosition).statementList();
          if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RBRACE)) {
            savedPosition.add();
            return new ForNode("eee");
          }
        }
      }
    }
    return null;
  }

  private WhileNode whileLoop() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.WHILE)) {
      savedPosition.add();
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LPAREN)) {
        savedPosition.add();
        currentPosition = savedPosition.getSavedPosition();
        CallFunctionNode callFunctionNode = new FunctionCall(utils,
            savedPosition).isFunctionCall();
        if (callFunctionNode == null) {
          savedPosition.setSavedPosition(currentPosition);
          ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
        }
        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RPAREN)) {
          savedPosition.add();
          if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LBRACE)) {
            savedPosition.add();
            new StatementList(utils, savedPosition).statementList();
            if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RBRACE)) {
              savedPosition.add();
              return new WhileNode("eeee");
            }
          }
        }
      }
    }
    return null;
  }

  public IfNode ifStatement() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IF)) {
      savedPosition.add();
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LPAREN)) {
        savedPosition.add();
        currentPosition = savedPosition.getSavedPosition();
        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.IDENTIFIER)
            || utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RECORD)
            || utils.matchIndex(savedPosition.getSavedPosition(), TokenType.BUILTINFUNCTION)) {
          savedPosition.add();
          CallFunctionNode callFunctionNode = new FunctionCall(utils,
              savedPosition).isFunctionCall();
          if (callFunctionNode == null) {
            savedPosition.setSavedPosition(currentPosition);
          }
        }
        ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RPAREN)) {
          savedPosition.add();
          if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LBRACE)) {
            savedPosition.add();
            new StatementList(utils, savedPosition).statementList();
            if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RBRACE)) {
              savedPosition.add();
              if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.ELSE)) {
                savedPosition.add();
                if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LBRACE)) {
                  savedPosition.add();
                  new StatementList(utils, savedPosition).statementList();
                  if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RBRACE)) {
                    savedPosition.add();
                    return new IfNode("eee");
                  }
                }
              }
              return new IfNode("eee");
            }
          }
        }
      }
    }
    return null;
  }


}
