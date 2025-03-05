package compiler.Parser.Grammar.Declaration.Global.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Lexer.Symbols.TypeSpecifier;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.AST.TypeSpecifierNode;
import compiler.Parser.Grammar.Declaration.Global.Node.GlobalNode;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Enum.SymbolsName;
import compiler.Parser.Utils.Utils;

public class Global {

  private final Utils utils;
  private Expression expression;

  public Global(Utils utils) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    expression = new Expression(utils);
  }

  public GlobalNode isGlobal() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_is(SymbolsName.Identifier.name())) {
      utils.match(SymbolsName.Identifier.name());
      IdentifierNode identifierNode = new IdentifierNode(utils.getPreviousSymbol().getToken());
      utils.match(SymbolsName.TypeSpecifier.name());
      TypeSpecifier typeSpecifier = new TypeSpecifier(utils.getPreviousSymbol().getToken(),
          utils.getPreviousSymbol().getLine_number());
      TypeSpecifierNode typeSpecifierNode = new TypeSpecifierNode(
          typeSpecifier.typeOfTypeSpecifier());
      utils.match(SymbolsName.Assignment.name());
      ExpressionNode expressionNode = expression.expression();
      utils.match(SymbolsName.SpecialSymbol.name());
      SpecialSymbolNode specialSymbolNode = new SpecialSymbolNode(
          utils.getPreviousSymbol().getToken());
      return new GlobalNode(identifierNode, typeSpecifierNode, expressionNode, specialSymbolNode);
    }
    return null;
  }
}
