package compiler.Parser.Grammar.Function;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Expression.Expression;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
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
  private LinkedList<ASTNode> ParameterListNode;
  private final String nodeName = "Parameters";

  public ParameterList(Utils utils, Position savedPosition) {
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

  private void parameterListTail()
      throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.COMMA, true)) {
      ParameterListNode.addLast(utils.getGenericNode());
      parameter();
      parameterListTail();
      return;
    }
    return;
  }

  private void parameter() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.TYPESPECIFIER, true) || utils.matchIndex(TokenType.RECORD,
        true)) {
      ParameterListNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
        ParameterListNode.addLast(utils.getGenericNode());
        return;
      }
      if (utils.matchIndex(TokenType.LPAREN, true)) {
        ParameterListNode.addLast(utils.getGenericNode());
        MainNode mainNode = isParameterList();
        ParameterListNode.addLast(mainNode);
        if (utils.matchIndex(TokenType.RPAREN, true)) {
          ParameterListNode.addLast(utils.getGenericNode());
          return;
        }
      }
      return;
    } else if (utils.matchIndex(TokenType.RPAREN, false)) {
      return;
    } else if (utils.lookahead_matches(expectedSymbolsGlobalArray, true)) {
      ParameterListNode.addAll(utils.getAstNodes());
      MainNode expressionNode = new Expression(utils, savedPosition).expression();
      ParameterListNode.addLast(expressionNode);
      if (utils.matchIndex(TokenType.RBRACKET, true)) {
        ParameterListNode.addLast(utils.getGenericNode());
        if (utils.matchIndex(TokenType.OF, true)) {
          ParameterListNode.addLast(utils.getGenericNode());
          if (utils.matchIndex(TokenType.TYPESPECIFIER, true) || utils.matchIndex(
              TokenType.RECORD,
              true)) {
            ParameterListNode.addLast(utils.getGenericNode());
            return;
          }
        }
      }
      return;
    } else {
      MainNode expressionNode = new Expression(utils, savedPosition).expression();
      ParameterListNode.addLast(expressionNode);
    }
  }
}
