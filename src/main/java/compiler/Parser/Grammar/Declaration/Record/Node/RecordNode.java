package compiler.Parser.Grammar.Declaration.Record.Node;

import compiler.Parser.AST.KeywordNode;
import compiler.Parser.AST.RecordNameNode;
import compiler.Parser.AST.SpecialSymbolNode;
import java.util.ArrayList;
import java.util.List;

public class RecordNode {

  private RecordNameNode recordNameNode;
  private SpecialSymbolNode specialSymbolNodeOpen;
  private SpecialSymbolNode specialSymbolNodeClose;

  private List<RecordFieldNode> children;
  private KeywordNode keywordNode;

  public RecordNode(RecordNameNode recordNameNode, KeywordNode keywordNode,
      SpecialSymbolNode specialSymbolNodeOpen, SpecialSymbolNode specialSymbolNodeClose,
      ArrayList<RecordFieldNode> fieldNodes) {
    this.recordNameNode = recordNameNode;
    this.specialSymbolNodeOpen = specialSymbolNodeOpen;
    this.specialSymbolNodeClose = specialSymbolNodeClose;
    this.keywordNode = keywordNode;
    this.children = fieldNodes;
  }


}
