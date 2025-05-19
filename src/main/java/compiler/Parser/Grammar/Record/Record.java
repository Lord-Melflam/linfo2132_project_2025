/*package compiler.Parser.Grammar.Declaration.Record.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interface.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Record {

  private final Utils utils;
  private RecordField recordField;
  LinkedList<ASTNode> recordNode;
  private final String nodeName = "Record";

  private Position savedPosition;
  private List<HashSet<TokenType>> expectedSymbolsRecord = List.of(
      new HashSet<>(Set.of(TokenType.RECORD)),
      new HashSet<>(Set.of(TokenType.REC)),
      new HashSet<>(Set.of(TokenType.LBRACE))

  );

  public Record(Utils utils, Position savedPosition)
      throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    this.savedPosition = savedPosition;
    recordNode = new LinkedList<>();
  }


  public MainNode isRecord() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_matches(expectedSymbolsRecord, true)) {
      recordNode.addAll(utils.getAstNodes());
      LinkedList<ASTNode> recordFields = new RecordField(utils,
          savedPosition).isRecordField();
      recordNode.addAll(recordFields);
      if (utils.matchIndex(TokenType.RBRACE, true)) {
        recordNode.addLast(utils.getGenericNode());
        return new MainNode(nodeName, recordNode);
      }
      return null;
    }
    return null;
  }
}*/

package compiler.Parser.Grammar.Record;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.ASTNode.MainNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Interfaces.ASTNode;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.LinkedList;

public class Record {

  private final Utils utils;
  private final String nodeName = "Record";
  private final Position savedPosition;
  private int line;

  public Record(Utils utils, Position savedPosition) {
    this.utils = utils;
    this.savedPosition = savedPosition;
    line = utils.getLine();
  }

  public MainNode isRecord() throws UnrecognisedTokenException, ParserException {
    LinkedList<ASTNode> recordNode = new LinkedList<>();
    if (utils.matchIndex(TokenType.RECORD, true)) {
      recordNode.addLast(utils.getGenericNode());
      if (utils.matchIndex(TokenType.REC, true)) {
        recordNode.addLast(utils.getGenericNode());
        if (utils.matchIndex(TokenType.LBRACE, true)) {
          //recordNode.addLast(utils.getGenericNode());
          LinkedList<ASTNode> recordFields = new RecordField(utils, savedPosition).isRecordField();
          if (recordFields != null) {
            recordNode.addLast(new MainNode("RecordFields", recordFields, line));
          }
          if (utils.matchIndex(TokenType.RBRACE, true)) {
            //recordNode.addLast(utils.getGenericNode());
          }

          return new MainNode(nodeName, recordNode, line);
        }
      }
    }
    utils.throwParserException();
    return null;
  }
}

