package compiler.Parser.Grammar.Function;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Grammar.Expression.Expression;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;

public class FunctionCall {

  private final Utils utils;
  private final Position savedPosition;
  LinkedList<ASTNode> FunctionCallNode;
  private final String nodeName = "FunctionCall";

  public FunctionCall(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    FunctionCallNode = new LinkedList<>();

  }

  public MainNode isFunctionCall() throws ParserException, UnrecognisedTokenException {
    MainNode functionCallNode = functionCall();
    if ((utils.matchIndex(TokenType.RPAREN, true))
        && functionCallNode != null) {
      FunctionCallNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.OPERATOR, false)) {
        MainNode expressionNode = new Expression(utils, savedPosition).expression();
      }
      return new MainNode(nodeName, FunctionCallNode);
    }
    return null;
  }

  private MainNode functionCall() throws ParserException, UnrecognisedTokenException {

    if (utils.matchIndex(TokenType.IDENTIFIER, true) || utils.matchIndex(TokenType.RECORD, true)
        || utils.matchIndex(TokenType.BUILTINFUNCTION, true)) {
      FunctionCallNode.addLast(utils.getGenericNode());

      if (utils.matchIndex(TokenType.LPAREN, true)) {
        FunctionCallNode.addLast(utils.getGenericNode());
        MainNode parameterListNode = new ParameterList(utils, savedPosition).isParameterList();
        FunctionCallNode.addLast(parameterListNode);

        return new MainNode(nodeName, FunctionCallNode);

      }
    }
    return null;
  }
}
