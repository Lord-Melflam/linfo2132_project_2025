package compiler.Parser.Grammar.Declaration;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Expression.Expression;
import compiler.Parser.Grammar.Function.ParameterList;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Declaration {

  private final Utils utils;
  private final Position savedPosition;
  private final List<HashSet<TokenType>> expectedSymbolsRParen = List.of(
      new HashSet<>(Set.of(TokenType.RPAREN)));
  private final List<HashSet<TokenType>> expectedSymbolsBuiltInFunction = List.of(
      new HashSet<>(Set.of(TokenType.BUILTINFUNCTION)), new HashSet<>(Set.of(TokenType.LPAREN)));

  private final List<HashSet<TokenType>> expectedSymbolsGlobalArray = List.of(
      new HashSet<>(Set.of(TokenType.ARRAY)), new HashSet<>(Set.of(TokenType.LBRACKET)));
  private LinkedList<ASTNode> constantNode;
  private String nodeName = "Declaration";

  public Declaration(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    constantNode = new LinkedList<>();
  }

  public Declaration(Utils utils, Position savedPosition, ASTNode identifier) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    constantNode = new LinkedList<>();
    constantNode.addLast(identifier);
  }

  public MainNode isConstant() throws UnrecognisedTokenException, ParserException {
    if (utils.matchIndex(TokenType.FINAL, true)) {
      constantNode.addLast(utils.getGenericNode());
      nodeName = "Constant";
      if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
        constantNode.addLast(utils.getGenericNode());
        return declaration();
      }
    }
    utils.throwParserException();
    return null;
  }

  @SuppressWarnings("unchecked")
  public MainNode declaration() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.TYPESPECIFIER, true) || utils.matchIndex(TokenType.RECORD,
        true)) {
      constantNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.ASSIGNMENT, true)) {
        constantNode.addLast(utils.getGenericNode());
        if (!utils.lookahead_matches(expectedSymbolsBuiltInFunction, false)) {
          if (utils.matchIndex(TokenType.PUNTUATION, true)) {
            constantNode.addLast(utils.getGenericNode());
          } else if (utils.matchIndex(TokenType.RECORD, false)) {
            MainNode callFunctionNode = new Expression(utils, savedPosition).expression();
            constantNode.addLast(callFunctionNode);
          } else if (utils.lookahead_matches(expectedSymbolsGlobalArray, true)) {
            constantNode.addAll(utils.getAstNodes());

            MainNode expressionNode = new Expression(utils, savedPosition).expression();
            constantNode.addLast(expressionNode);
            if (utils.matchIndex(TokenType.RBRACKET, true)) {
              constantNode.addLast(utils.getGenericNode());
              if (utils.matchIndex(TokenType.OF, true)) {
                constantNode.addLast(utils.getGenericNode());
                if (utils.matchIndex(TokenType.TYPESPECIFIER, true) || utils.matchIndex(
                    TokenType.RECORD, true)) {
                  constantNode.addLast(utils.getGenericNode());
                  if (utils.matchIndex(TokenType.SEMICOLON, true)) {
                    constantNode.addLast(utils.getGenericNode());
                    return new MainNode(nodeName, constantNode);
                  }
                }
              }
            }
          } else {

            MainNode expressionNode = new Expression(utils, savedPosition).expression();
            constantNode.addLast(expressionNode);
          }
        } else {
          MainNode parameterList = new ParameterList(utils, savedPosition).isParameterList();
          constantNode.addLast(parameterList);
          if (utils.lookahead_matches(expectedSymbolsRParen, true)) {
            constantNode.addAll(utils.getAstNodes());
          }
        }
        if (utils.matchIndex(TokenType.SEMICOLON, true)) {
          constantNode.addLast(utils.getGenericNode());
          return new MainNode(nodeName, constantNode);
        } else {
          /*todo*/
        }
      } else {
        if (utils.matchIndex(TokenType.SEMICOLON, true)) {
          constantNode.addLast(utils.getGenericNode());
          return new MainNode(nodeName, constantNode);
        }
      }
    }
    utils.throwParserException();
    return null;
  }
}
