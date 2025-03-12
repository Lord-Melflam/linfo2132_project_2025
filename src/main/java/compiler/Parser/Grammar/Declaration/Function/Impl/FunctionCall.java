package compiler.Parser.Grammar.Declaration.Function.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
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

  /*TODO*/
  public MainNode isFunctionCall() throws ParserException, UnrecognisedTokenException {
    MainNode functionCallNode = functionCall();

    if (utils.matchIndex(TokenType.RPAREN, true)
        && functionCallNode != null) {
      //FunctionCallNode.addLast(functionCallNode);
      FunctionCallNode.addLast(utils.getGenericNode());
      return new MainNode(nodeName, FunctionCallNode);
    }
    return null;
  }

  private MainNode functionCall() throws ParserException, UnrecognisedTokenException {

    if (utils.matchIndex(TokenType.IDENTIFIER, true)) {
      FunctionCallNode.addLast(utils.getGenericNode());
    }
    if (utils.matchIndex(TokenType.RECORD, true)) {
      FunctionCallNode.addLast(utils.getGenericNode());
    }
    if (utils.matchIndex(TokenType.BUILTINFUNCTION, true)) {
      FunctionCallNode.addLast(utils.getGenericNode());
    }
    if (utils.matchIndex(TokenType.LPAREN, true)) {
      FunctionCallNode.addLast(utils.getGenericNode());
      MainNode parameterListNode = new ParameterList(utils,
          savedPosition).isParameterList();
      FunctionCallNode.addLast(parameterListNode);

      return new MainNode(nodeName, FunctionCallNode);

    }
    return null;
  }
}
