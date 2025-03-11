package compiler.Parser.Grammar.Declaration.Constant.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Constant.Node.ConstantNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.FunctionCall;
import compiler.Parser.Grammar.Declaration.Function.Impl.ParameterList;
import compiler.Parser.Grammar.Declaration.Function.Node.CallFunctionNode;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Constant {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition;
  private final List<HashSet<TokenType>> expectedSymbolsRParen = List.of(
      new HashSet<>(Set.of(TokenType.RPAREN)));
  private final List<HashSet<TokenType>> expectedSymbolsBuiltInFunction = List.of(
      new HashSet<>(Set.of(TokenType.BUILTINFUNCTION)), new HashSet<>(Set.of(TokenType.LPAREN)));
  private final List<HashSet<TokenType>> expectedSymbolsGlobalAlternative = List.of(
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.TYPESPECIFIER, TokenType.RECORD)),
      new HashSet<>(Set.of(TokenType.SEMICOLON)));
  private final List<HashSet<TokenType>> expectedSymbolsGlobal = List.of(
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.TYPESPECIFIER, TokenType.RECORD)),
      new HashSet<>(Set.of(TokenType.ASSIGNMENT)));

  public Constant(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
  }

  public ConstantNode isConstant() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.FINAL)) {
      savedPosition.add();
    }
    if (utils.lookahead_matches(savedPosition.getSavedPosition(), expectedSymbolsGlobal)) {
      savedPosition.add(expectedSymbolsGlobal);
      if (!utils.lookahead_matches(savedPosition.getSavedPosition(),
          expectedSymbolsBuiltInFunction)) {
        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.PUNTUATION)) {
          savedPosition.add();
        } else if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RECORD)) {
          new FunctionCall(utils, savedPosition).isFunctionCall();
        } else {
          currentPosition = savedPosition.getSavedPosition();
          CallFunctionNode callFunctionNode = new FunctionCall(utils,
              savedPosition).isFunctionCall();
          if (callFunctionNode == null) {
            savedPosition.setSavedPosition(currentPosition);
            ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
          }
        }
      } else {
        savedPosition.add(expectedSymbolsBuiltInFunction);
        new ParameterList(utils, savedPosition).isParameterList();
        if (utils.lookahead_matches(savedPosition.getSavedPosition(), expectedSymbolsRParen)) {
          savedPosition.add(expectedSymbolsRParen);
        }
      }
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.SEMICOLON)) {
        savedPosition.add();
        return new ConstantNode(null);
      }
    } else if (utils.lookahead_matches(savedPosition.getSavedPosition(),
        expectedSymbolsGlobalAlternative)) {
      savedPosition.add(expectedSymbolsGlobalAlternative);
      return new ConstantNode(null);
    }
    return null;
  }
}
