package compiler.Parser.Grammar.Expression.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.IdentifierNode;
import compiler.Parser.AST.LiteralNode;
import compiler.Parser.AST.OperatorNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Expression.BinaryOperator;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Grammar.Expression.PrimaryOperator;
import compiler.Parser.Utils.Enum.SymbolsName;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Utils;

public class Expression {

  private Utils utils;

  public Expression(Utils utils) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
  }

  public ExpressionNode expression() throws UnrecognisedTokenException, ParserException {
    ASTNode primary = primary();
    ASTNode expressionTail = expressionTail();

    return new ExpressionNode(primary, expressionTail);
  }

  private ASTNode expressionTail() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_is(SymbolsName.Operator.name())) {
      return new ExpressionNode(binary(), primary(), expressionTail());
    }
    return null;
  }

  private ASTNode binary() throws UnrecognisedTokenException, ParserException {
    utils.match(SymbolsName.Operator.name());
    return new BinaryOperator(new OperatorNode(utils.getCurrentSymbol().getToken()));
  }

  private ASTNode primary() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_is(SymbolsName.Literal.name())) {
      utils.match(SymbolsName.Literal.name());
      return new PrimaryOperator(new LiteralNode(utils.getPreviousSymbol().getToken()));
    } else if (utils.lookahead_is(SymbolsName.Identifier.name())) {
      utils.match(SymbolsName.Identifier.name());
      return new PrimaryOperator(new IdentifierNode(utils.getCurrentSymbol().getToken()));
    } else if (utils.lookahead_is(SymbolsName.SpecialSymbol.name())) {
      utils.match(SymbolsName.SpecialSymbol.name());
      ASTNode specialSymbolOpen = new SpecialSymbolNode(utils.getCurrentSymbol().getToken());
      ASTNode expression = expression();
      ASTNode specialSymbolClose =
          new SpecialSymbolNode(utils.getCurrentSymbol().getToken());
      utils.match(SymbolsName.SpecialSymbol.name());
      return new PrimaryOperator(specialSymbolOpen, expression, specialSymbolClose);
    }
    return null;
  }
}
