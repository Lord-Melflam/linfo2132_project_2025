package compiler.Parser.Grammar.Declaration.Record.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordFieldNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class RecordField {

  private Utils utils;
  private ArrayList<RecordFieldNode> fields = new ArrayList<RecordFieldNode>();
  private Position savedPosition;
  private List<HashSet<TokenType>> expectedSymbolsRecordField = List.of(
      new HashSet<>(Set.of(TokenType.IDENTIFIER)),
      new HashSet<>(Set.of(TokenType.TYPESPECIFIER, TokenType.RECORD)),
      new HashSet<>(Set.of(TokenType.SEMICOLON))
  );
  LinkedList<ASTNode> recordFieldNode;
  private final String nodeName = "RecordField";

  public RecordField(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    recordFieldNode = new LinkedList<>();

  }

  public LinkedList<ASTNode> isRecordField()
      throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_matches(expectedSymbolsRecordField, true)) {
      recordFieldNode.addAll(utils.getAstNodes());

      LinkedList<ASTNode> moreFields = isRecordField();
      if (moreFields != null) {
        recordFieldNode.addAll(moreFields);
      }
    }

    if (recordFieldNode.isEmpty()) {
      return null;
    }
    return recordFieldNode;
  }


}
