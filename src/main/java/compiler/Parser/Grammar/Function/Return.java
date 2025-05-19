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

public class Return {

  private final Utils utils;
  private final Position savedPosition;
  private final LinkedList<ASTNode> returnNode;
  private final String nodeName = "Return";
  private int line;

  public Return(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    returnNode = new LinkedList<>();
    line = utils.getLine();
  }

  public MainNode isReturn() throws ParserException, UnrecognisedTokenException {
    if (utils.matchIndex(TokenType.RETURN, true)) {
      // returnNode.addLast(utils.getGenericNode());
      MainNode expressionNode = new Expression(utils, savedPosition).expression();
      returnNode.addLast(expressionNode);
      if (utils.matchIndex(TokenType.SEMICOLON, true)) {
        //returnNode.addLast(utils.getGenericNode());
        return new MainNode(nodeName, returnNode, line);
      }
    }
    utils.throwParserException();
    return null;
  }

}
