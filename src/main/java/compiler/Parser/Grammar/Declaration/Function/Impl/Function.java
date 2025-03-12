package compiler.Parser.Grammar.Declaration.Function.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.TypeSpecifierNode;
import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;
import compiler.Parser.Grammar.Statement.Impl.StatementList;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Function {

  private final Utils utils;
  private ParameterList parameterList;
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
  private final List<HashSet<TokenType>> expectedSymbolsFunctionBuiltIn = List.of(
      new HashSet<>(Set.of(TokenType.BUILTINFUNCTION)),
      new HashSet<>(Set.of(TokenType.LPAREN))
  );
  LinkedList<ASTNode> functionNode;
  private final String nodeName = "Function";

  public Function(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    functionNode = new LinkedList<>();

  }

  public MainNode isFunction() throws UnrecognisedTokenException, ParserException {

    if (utils.lookahead_matches(expectedSymbolsFunctionAlternative, false)) {
/*
      savedPosition.add(expectedSymbolsFunctionAlternative);
*/
      /*todo*/
      //functionNode.addAll(utils.getAstNodes());
    } else if (utils.lookahead_matches(expectedSymbolsFunction, true)) {
      functionNode.addAll(utils.getAstNodes());
      MainNode parameterListNode = new ParameterList(utils, savedPosition).isParameterList();
      functionNode.addLast(parameterListNode);

      if (utils.matchIndex(TokenType.RPAREN, true)) {
        functionNode.addLast(utils.getGenericNode());
      }
    }
    if (!utils.matchIndex(TokenType.TYPESPECIFIER, false)) {
      functionNode.addLast(new TypeSpecifierNode("void"));
    } else {
      utils.matchIndex(TokenType.TYPESPECIFIER, true);
      functionNode.addLast(utils.getGenericNode());
    }
    if (utils.matchIndex(TokenType.LBRACE, true)) {
      functionNode.addLast(utils.getGenericNode());
      StatementList statementList = new StatementList(utils, savedPosition);
      MainNode astNode = statementList.statementList();
      functionNode.addLast(astNode);
      if (utils.matchIndex(TokenType.RBRACE, true)) {
        functionNode.addLast(utils.getGenericNode());
        MainNode mainNode = new MainNode(nodeName, functionNode);
        return mainNode;
      }
    }

    return null;
  }
}
