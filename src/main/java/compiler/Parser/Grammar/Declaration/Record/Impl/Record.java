package compiler.Parser.Grammar.Declaration.Record.Impl;

import compiler.Exceptions.Lexer.UnrecognisedTokenException;
import compiler.Exceptions.Parser.ParserException;
import compiler.Parser.AST.KeywordNode;
import compiler.Parser.AST.RecordNameNode;
import compiler.Parser.AST.SpecialSymbolNode;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordFieldNode;
import compiler.Parser.Grammar.Declaration.Record.Node.RecordNode;
import compiler.Parser.Utils.Enum.SymbolsName;
import compiler.Parser.Utils.Utils;
import java.util.ArrayList;

public class Record {

  private final Utils utils;
  private RecordField recordField;

  public Record(Utils utils) throws UnrecognisedTokenException, ParserException {
    this.utils = utils;
    recordField = new RecordField(utils);
  }

  public RecordNode isRecord() throws UnrecognisedTokenException, ParserException {
    if (utils.lookahead_is(SymbolsName.Record.name())) {
      utils.match(SymbolsName.Record.name());
      RecordNameNode recordNameNode = new RecordNameNode(utils.getPreviousSymbol().getToken());
      utils.match(SymbolsName.Keyword.name());
      KeywordNode keywordNode = new KeywordNode(utils.getPreviousSymbol().getToken());
      utils.match(SymbolsName.SpecialSymbol.name());
      SpecialSymbolNode specialSymbolOpen = new SpecialSymbolNode(
          utils.getCurrentSymbol().getToken());
      ArrayList<RecordFieldNode> recordFields = recordField.isRecordField();
      utils.match(SymbolsName.SpecialSymbol.name());
      SpecialSymbolNode specialSymbolClose = new SpecialSymbolNode(
          utils.getCurrentSymbol().getToken());
      return new RecordNode(recordNameNode, keywordNode, specialSymbolOpen, specialSymbolClose,
          recordFields);
    }
    return null;
  }
}
