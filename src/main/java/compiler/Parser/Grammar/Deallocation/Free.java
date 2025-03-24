package compiler.Parser.Grammar.Deallocation;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Free {

  private final Utils utils;
  private final List<HashSet<TokenType>> expectedSymbolsFree = List.of(
      new HashSet<>(Set.of(TokenType.FREE)),
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.SEMICOLON)));
  LinkedList<ASTNode> freeNode;
  private final String nodeName = "Deallocation";

  public Free(Utils utils, Position savedPosition) {
    this.utils = utils;
    freeNode = new LinkedList<>();
  }

  public MainNode free() throws ParserException, UnrecognisedTokenException {
    if (utils.lookahead_matches(expectedSymbolsFree, true)) {
      freeNode.addAll(utils.getAstNodes());
      return new MainNode(nodeName, freeNode);
    }
    utils.throwParserException();
    return null;
  }

}
