package compiler.Parser.Grammar.Function;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.ASTNode.TypeSpecifierNode;
import compiler.Parser.Grammar.Expression.Expression;
import compiler.Parser.Grammar.Statement.StatementList;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Function {

  private final Utils utils;
  private final Position savedPosition;
  private final List<HashSet<TokenType>> expectedSymbolsFunction = List.of(
      new HashSet<>(Set.of(TokenType.FUN)),
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.LPAREN))
  );
  private final List<HashSet<TokenType>> expectedSymbolsFunctionAlternative = List.of(
      new HashSet<>(Set.of(TokenType.FUN)),
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.LPAREN)),
      new HashSet<>(Set.of(TokenType.RPAREN))
  );
  LinkedList<ASTNode> functionNode;
  private final String nodeName = "Function";
  private int currentPosition;

  public Function(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    functionNode = new LinkedList<>();
    this.currentPosition = 0;

  }

  public MainNode isFunction() throws UnrecognisedTokenException, ParserException {

    if (utils.lookahead_matches(expectedSymbolsFunctionAlternative, false)) {

      savedPosition.add(expectedSymbolsFunctionAlternative);
      functionNode.addAll(utils.getAstNodes());
    } else if (utils.lookahead_matches(expectedSymbolsFunction, true)) {
      functionNode.addAll(utils.getAstNodes());
      MainNode parameterListNode = new ParameterList(utils, savedPosition).isParameterList();
      functionNode.addLast(parameterListNode);

      if (utils.matchIndex(TokenType.RPAREN, true)) {
        functionNode.addLast(utils.getGenericNode());
      }
    }
    if (utils.matchIndex(
        TokenType.TYPESPECIFIER, true)) {
      functionNode.addLast(utils.getGenericNode());
    } else {
      functionNode.addLast(new TypeSpecifierNode("void"));

    }
    if (utils.matchIndex(TokenType.LBRACE, true)) {
      functionNode.addLast(utils.getGenericNode());
      StatementList statementList = new StatementList(utils, savedPosition);
      MainNode astNode = statementList.statementList();
      functionNode.addLast(astNode);

      if (utils.matchIndex(TokenType.ASSIGNMENT, true)) {
        functionNode.addLast(utils.getGenericNode());
        currentPosition = savedPosition.getSavedPosition();
        MainNode callFunctionNode = new FunctionCall(utils,
            savedPosition).isFunctionCall();
        if (callFunctionNode == null) {
          savedPosition.setSavedPosition(currentPosition);
          MainNode expressionNode = new Expression(utils, savedPosition).expression();
          functionNode.addLast(expressionNode);
        } else {
          functionNode.addLast(callFunctionNode);
        }
        if (utils.matchIndex(TokenType.SEMICOLON, true)) {
          if (utils.matchIndex(TokenType.RBRACE, true)) {
            functionNode.addLast(utils.getGenericNode());
            return new MainNode(nodeName, functionNode);
          }
        }
      }
      if (utils.matchIndex(TokenType.RBRACE, true)) {
        functionNode.addLast(utils.getGenericNode());
        return new MainNode(nodeName, functionNode);
      }
    }

    return null;
  }
}
