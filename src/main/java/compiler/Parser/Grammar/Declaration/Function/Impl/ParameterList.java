package compiler.Parser.Grammar.Declaration.Function.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Assignment.Impl.Assignment;
import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;
import compiler.Parser.Grammar.Declaration.Function.Node.ParameterListTailNode;
import compiler.Parser.Grammar.Declaration.Function.Node.ParameterNode;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ParameterList {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition;
  private final List<HashSet<TokenType>> expectedSymbolsGlobalArray = List.of(
      new HashSet<>(Set.of(TokenType.ARRAY)),
      new HashSet<>(Set.of(TokenType.LBRACKET)));
  LinkedList<ASTNode> ParameterListNode;
  private final String nodeName = "ParameterList";

  public ParameterList(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
    ParameterListNode = new LinkedList<>();
  }

  public MainNode isParameterList() throws ParserException, UnrecognisedTokenException {
    parameter();
    parameterListTail();
    return new MainNode(nodeName, ParameterListNode);
  }

  private ParameterListTailNode parameterListTail()
      throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.COMMA, true)) {
      ParameterListNode.addLast(utils.getGenericNode());
      return new ParameterListTailNode(parameter(), parameterListTail());
    }
    return null;
  }

  private ParameterNode parameter() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.TYPESPECIFIER, true) || utils.matchIndex(TokenType.RECORD,
        true)) {
      ParameterListNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
        ParameterListNode.addLast(utils.getGenericNode());
        return new ParameterNode(null, null);
      }
      if (utils.matchIndex(TokenType.LPAREN, true)) {
        ParameterListNode.addLast(utils.getGenericNode());
        MainNode mainNode = isParameterList();
        ParameterListNode.addLast(mainNode);
        if (utils.matchIndex(TokenType.RPAREN, true)) {
          ParameterListNode.addLast(utils.getGenericNode());
          return new ParameterNode(null, null);
        }
      }
      return null;
      /*todo*/
    } else if (utils.matchIndex(TokenType.RPAREN, false)) {
      //ParameterListNode.addLast(utils.getGenericNode());
      return new ParameterNode(null, null);

    } else if (utils.lookahead_matches(expectedSymbolsGlobalArray, true)) {
      ParameterListNode.addAll(utils.getAstNodes());

      currentPosition = savedPosition.getSavedPosition();
      MainNode callFunctionNode = new FunctionCall(utils,
          savedPosition).isFunctionCall();
      if (callFunctionNode == null) {
        savedPosition.setSavedPosition(currentPosition);
        MainNode expressionNode = new Expression(utils, savedPosition).expression();
        ParameterListNode.addLast(expressionNode);
      } else {
        ParameterListNode.addLast(callFunctionNode);
      }
        /*  arr int[] = array [10] of int;
    people Person[] = array [2] of Person;*/
      if (utils.matchIndex(TokenType.RBRACKET, true)) {
        ParameterListNode.addLast(utils.getGenericNode());
        if (utils.matchIndex(TokenType.OF, true)) {
          ParameterListNode.addLast(utils.getGenericNode());
          if (utils.matchIndex(TokenType.TYPESPECIFIER, true) || utils.matchIndex(
              TokenType.RECORD,
              true)) {
            ParameterListNode.addLast(utils.getGenericNode());
            return new ParameterNode(null, null);
          }
        }
      }
      return null;
    } else {
      currentPosition = savedPosition.getSavedPosition();
      MainNode callFunctionNode = new FunctionCall(utils, savedPosition).isFunctionCall();
      if (callFunctionNode == null) {
        savedPosition.setSavedPosition(currentPosition);
        MainNode assignmentExpressionNode = new Assignment(utils,
            savedPosition).assignment();
        if (assignmentExpressionNode == null) {
          savedPosition.setSavedPosition(currentPosition);
          MainNode expressionNode = new Expression(utils, savedPosition).expression();
          ParameterListNode.addLast(expressionNode);

        } else {
          ParameterListNode.addLast(assignmentExpressionNode);
        }
      } else {
        ParameterListNode.addLast(callFunctionNode);
      }
      return new ParameterNode(null, null);

    }
  }
}
