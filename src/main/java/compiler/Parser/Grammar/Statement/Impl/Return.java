package compiler.Parser.Grammar.Statement.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.KeywordNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Declaration.Function.Impl.FunctionCall;
import compiler.Parser.Grammar.Declaration.Function.Node.CallFunctionNode;
import compiler.Parser.Grammar.Expression.Impl.Expression;
import compiler.Parser.Grammar.Expression.Node.ExpressionNode;
import compiler.Parser.Grammar.Statement.Node.ReturnNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;

public class Return {

  private final Utils utils;
  private final Position savedPosition;
  private int currentPosition;

  public Return(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
  }

  public ReturnNode isReturn() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RETURN)) {
/*
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Keyword.name());
*/
      savedPosition.add();
      KeywordNode keywordNode = new KeywordNode("hgfcdjfhgdf");
      currentPosition = savedPosition.getSavedPosition();
      CallFunctionNode callFunctionNode = new FunctionCall(utils,
          savedPosition).isFunctionCall();
      if (callFunctionNode == null) {
        savedPosition.setSavedPosition(currentPosition);
        ExpressionNode expressionNode = new Expression(utils, savedPosition).expression();
      }
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.SEMICOLON)) {
        savedPosition.add();
        SpecialSymbolNode specialSymbolNode = new SpecialSymbolNode(null);
        return new ReturnNode(keywordNode, null, specialSymbolNode);
      }
    }
    return null;
  }

}
