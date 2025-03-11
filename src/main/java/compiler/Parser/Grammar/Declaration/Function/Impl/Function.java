package compiler.Parser.Grammar.Declaration.Function.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Function.Node.FunctionNode;
import compiler.Parser.Grammar.Declaration.Function.Node.ParameterListNode;
import compiler.Parser.Grammar.Statement.Impl.StatementList;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
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

  public Function(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
/*
    parameterList = new ParameterList(utils);
*/
  }

  public FunctionNode isFunction() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_matches(savedPosition.getSavedPosition(), expectedSymbolsFunction)) {
      savedPosition.add(expectedSymbolsFunction);
      ParameterListNode parameterListNode = new ParameterList(utils,
          savedPosition).isParameterList();
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RPAREN)) {
        savedPosition.add();
        if (!utils.matchIndex(savedPosition.getSavedPosition(), TokenType.TYPESPECIFIER)) {
          System.out.println("void type");
        } else {
          utils.matchIndex(savedPosition.getSavedPosition(), TokenType.TYPESPECIFIER);
          savedPosition.add();
        }
        if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.LBRACE)) {
          savedPosition.add();
/*
      ASTNode statementList = new StatementList(utils, savedPosition).statementList();
*/
          StatementList statementList = new StatementList(utils, savedPosition);
          ASTNode astNode = statementList.statementList();
          if (utils.matchIndex(savedPosition.getSavedPosition(),
              TokenType.RBRACE)) {
            savedPosition.add();
            return new FunctionNode();
          }
        }
      }
    }
    return null;
/*    if (utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Keyword.name())) {
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Keyword.name());
      savedPosition.add();
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Identifier.name());
      savedPosition.add();
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.SpecialSymbol.name());
      savedPosition.add();

      ParameterListNode parameterListNode = new ParameterList(utils,
          savedPosition).isParameterList();
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.SpecialSymbol.name());
      savedPosition.add();

      if (!utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.TypeSpecifier.name())) {
        System.out.println("void type");
      } else {
        utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.TypeSpecifier.name());
        savedPosition.add();
      }
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.SpecialSymbol.name());
      savedPosition.add();
*//*
      ASTNode statementList = new StatementList(utils, savedPosition).statementList();
*//*
      StatementList statementList = new StatementList(utils, savedPosition);
      ASTNode astNode = statementList.statementList();
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.SpecialSymbol.name());
      savedPosition.add();

      return new FunctionNode();
    }*/
/*
    return null;
*/
  }
}
