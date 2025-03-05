package compiler.Parser.Grammar.Declaration.Constant.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Constant.Node.ConstantNode;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Utils.Enum.SymbolsName;
import compiler.Parser.Utils.Utils;

public class Constant {

  private final Utils utils;
  private Expression expression;

  public Constant(Utils utils) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    expression = new Expression(utils);
  }

  public ConstantNode isConstant() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_is(SymbolsName.Keyword.name())) {
      utils.match(SymbolsName.Keyword.name());
      utils.match(SymbolsName.Identifier.name());
      utils.match(SymbolsName.TypeSpecifier.name());
      utils.match(SymbolsName.Assignment.name());
      ExpressionNode expressionNode = expression.expression();
      utils.match(SymbolsName.SpecialSymbol.name());
      return new ConstantNode(expressionNode);
    }
    return null;
  }
}
