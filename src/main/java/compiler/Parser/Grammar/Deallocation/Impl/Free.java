package compiler.Parser.Grammar.Deallocation.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Constant.Node.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Free {

  private Utils utils;
  private int currentPosition;
  private Position savedPosition;
  private final List<HashSet<TokenType>> expectedSymbolsFree = List.of(
      new HashSet<>(Set.of(TokenType.FREE)),
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.SEMICOLON)));
  LinkedList<ASTNode> freeNode;
  private final String nodeName = "Free";

  public Free(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
    freeNode = new LinkedList<>();
  }

  public MainNode free() throws ParserException, UnrecognisedTokenException {
    if (utils.lookahead_matches(expectedSymbolsFree, true)) {
      freeNode.addAll(utils.getAstNodes());
      return new MainNode(nodeName, freeNode);
    }
    return null;
  }

}
