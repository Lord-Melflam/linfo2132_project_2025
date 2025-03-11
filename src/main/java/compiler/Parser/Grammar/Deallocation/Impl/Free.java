package compiler.Parser.Grammar.Deallocation.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Deallocation.Node.FreeNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
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

  public Free(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    this.currentPosition = 0;
  }

  public FreeNode free() throws ParserException, UnrecognisedTokenException {
    if (utils.lookahead_matches(savedPosition.getSavedPosition(), expectedSymbolsFree)) {
      savedPosition.add(expectedSymbolsFree);
      return new FreeNode("ee");
    }
    return null;
  }

}
