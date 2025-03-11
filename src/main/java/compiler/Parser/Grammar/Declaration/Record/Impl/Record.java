package compiler.Parser.Grammar.Declaration.Record.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordFieldNode;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordNode;
import compiler.Parser.Utils.Enum.TokenType;
import compiler.Parser.Utils.Position;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Record {

  private final Utils utils;
  private RecordField recordField;

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
  }


  public RecordNode isRecord() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_matches(savedPosition.getSavedPosition(), expectedSymbolsRecord)) {
      savedPosition.add(expectedSymbolsRecord);
      ArrayList<RecordFieldNode> recordFields = new RecordField(utils,
          savedPosition).isRecordField();
      if (utils.matchIndex(savedPosition.getSavedPosition(), TokenType.RBRACE)) {
        savedPosition.add();
        return new RecordNode(null, null, null, null,
            recordFields);
      }
      return null;
    }
    return null;
   /*   if (utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Record.name())) {
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Record.name());
      savedPosition.add();
      RecordNameNode recordNameNode = new RecordNameNode(null);
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.Keyword.name());
      savedPosition.add();
      KeywordNode keywordNode = new KeywordNode(null);
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.SpecialSymbol.name());
      savedPosition.add();
      SpecialSymbolNode specialSymbolOpen = new SpecialSymbolNode(null);
      ArrayList<RecordFieldNode> recordFields = new RecordField(utils,
          savedPosition).isRecordField();
      utils.matchIndex(savedPosition.getSavedPosition(), SymbolsName.SpecialSymbol.name());
      savedPosition.add();

      SpecialSymbolNode specialSymbolClose = new SpecialSymbolNode(null);
      return new RecordNode(recordNameNode, keywordNode, specialSymbolOpen, specialSymbolClose,
          recordFields);
    }
    return null;*/
  }
}
